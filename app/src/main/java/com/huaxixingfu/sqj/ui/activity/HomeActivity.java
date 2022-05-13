package com.huaxixingfu.sqj.ui.activity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.diskkiller.base.FragmentPagerAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.other.DoubleClickHelper;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.push.service.ImService;
import com.huaxixingfu.sqj.ui.adapter.NavigationAdapter;
import com.huaxixingfu.sqj.ui.fragment.FragmentHome;
import com.huaxixingfu.sqj.ui.fragment.FragmentLife;
import com.huaxixingfu.sqj.ui.fragment.FragmentMsgList;
import com.huaxixingfu.sqj.ui.fragment.FragmentMy;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.NetWorkUtils;
import com.huaxixingfu.sqj.utils.SPManager;

import java.util.List;

/**
 *    desc   : 首页界面
 */
public final class HomeActivity extends AppActivity
        implements NavigationAdapter.OnNavigationListener {

    private static final String INTENT_KEY_IN_FRAGMENT_INDEX = "fragmentIndex";
    private static final String INTENT_KEY_IN_FRAGMENT_CLASS = "fragmentClass";

    private ViewPager mViewPager;
    private RecyclerView mNavigationView;

    private NavigationAdapter mNavigationAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;

    private NetworkConnectChangedReceiver networkConnectChangedReceiver;

    public static void start(Context context) {
        start(context, FragmentHome.class);
    }

    public static void start(Context context, Class<? extends AppFragment<?>> fragmentClass) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(INTENT_KEY_IN_FRAGMENT_CLASS, fragmentClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_home_pager);
        mNavigationView = findViewById(R.id.rv_home_navigation);

        mNavigationAdapter = new NavigationAdapter(this);
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_index),
                ContextCompat.getDrawable(this, R.drawable.home_home_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_found),
                ContextCompat.getDrawable(this, R.drawable.home_found_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_message),
                ContextCompat.getDrawable(this, R.drawable.home_message_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_me),
                ContextCompat.getDrawable(this, R.drawable.home_me_selector)));
        mNavigationAdapter.setOnNavigationListener(this);
        mNavigationView.setAdapter(mNavigationAdapter);

        //监听网络状态的变化
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkConnectChangedReceiver, intentFilter);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(FragmentHome.newInstance());
        mPagerAdapter.addFragment(FragmentLife.newInstance());
        mPagerAdapter.addFragment(FragmentMsgList.newInstance());
        mPagerAdapter.addFragment(FragmentMy.newInstance());
        mViewPager.setAdapter(mPagerAdapter);
        onNewIntent(getIntent());
        initIM();
    }

    private void initIM() {
        if (SPManager.instance(getActivity()).isLogin()) {

            WebSocketManager.getInstance().init(SPManager.instance(getApplicationContext()).getToken(), new WebSocketManager.ConnectStateListener() {
                @Override
                public void onConnectSuccess() {

                }

                @Override
                public void onConnectFailed() {
                    runOnUiThread(() -> Toast.makeText(getContext(), "IM登录失败", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onClose() {

                }

                @Override
                public void onMessage(String text) {
                    LogUtil.e("aaaaaaaa_SplashWebSocket", "websocke------" + text);
                    if (WebSocketManager.getCommand(text) == WebSocketManager.COMMAND_LOGIN_RESP) {
                        if (WebSocketManager.getCode(text) == WebSocketManager.LOGIN_SUCCESS) {

                        } else {
                            Toast.makeText(getActivity(), "IM登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public static class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是网络状态发生改变
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean networkFlag = NetWorkUtils.getInstances().isNetSystemUsable(context);
                WebSocketManager webSocketManager = WebSocketManager.getInstance();
                //如果当前未连接上websocket 且 已达到最大尝试重连次数 且 当前网络正常，则尝试再次重连
                if (SPManager.instance(context).isLogin() && !webSocketManager.isConnect() && webSocketManager.isMaxReconnectCount() && networkFlag) {
                    webSocketManager.refreshReconnectCount().reconnect();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //只有登录才尝试开启后台服务
        if (!isServiceRunning(ImService.class.getName(), getContext()) && SPManager.instance(getApplicationContext()).isLogin()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getContext(), ImService.class));
            } else {
                startService(new Intent(getContext(), ImService.class));
            }
        }
    }

    public boolean isServiceRunning(String className, Context context) {
        //进程管理者
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //获取进程中正在运行的服务集合
        List<android.app.ActivityManager.RunningServiceInfo> runningservice = manager.getRunningServices(1000);
        //遍历
        for (android.app.ActivityManager.RunningServiceInfo runningServiceInfo : runningservice) {
            //获取正在运行的服务的标识
            ComponentName service = runningServiceInfo.service;
            //获取正在运行的服务的全类型
            String name = service.getClassName();
            //判断
            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switchFragment(mPagerAdapter.getFragmentIndex(getSerializable(INTENT_KEY_IN_FRAGMENT_CLASS)));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存当前 Fragment 索引位置
        outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 恢复当前 Fragment 索引位置
        switchFragment(savedInstanceState.getInt(INTENT_KEY_IN_FRAGMENT_INDEX));
    }

    private void switchFragment(int fragmentIndex) {
        if (fragmentIndex == -1) {
            return;
        }

        switch (fragmentIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                mViewPager.setCurrentItem(fragmentIndex);
                mNavigationAdapter.setSelectedPosition(fragmentIndex);
                break;
            default:
                break;
        }
    }

    /**
     * {@link NavigationAdapter.OnNavigationListener}
     */

    @Override
    public boolean onNavigationItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                mViewPager.setCurrentItem(position);
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 指定导航栏背景颜色
                .navigationBarColor(R.color.white);
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint);
            return;
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false);
        postDelayed(() -> {
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities();
            // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
            // System.exit(0);
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().close();
        WebSocketManager.release();
        mViewPager.setAdapter(null);
        mNavigationView.setAdapter(null);
        mNavigationAdapter.setOnNavigationListener(null);
        unregisterReceiver(networkConnectChangedReceiver);
    }
}