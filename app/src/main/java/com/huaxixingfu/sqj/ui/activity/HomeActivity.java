package com.huaxixingfu.sqj.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.diskkiller.base.BaseDialog;
import com.diskkiller.base.FragmentPagerAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.OnHttpListener;
import com.gyf.immersionbar.ImmersionBar;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.bean.UserData;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.AppVersionApi;
import com.huaxixingfu.sqj.http.api.PushTokenBind;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.other.DoubleClickHelper;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.push.service.ImService;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.me.PersonalDataActivity;
import com.huaxixingfu.sqj.ui.adapter.NavigationAdapter;
import com.huaxixingfu.sqj.ui.dialog.MessageDialog;
import com.huaxixingfu.sqj.ui.dialog.UpdateDialog;
import com.huaxixingfu.sqj.ui.fragment.FragmentHome;
import com.huaxixingfu.sqj.ui.fragment.FragmentLife;
import com.huaxixingfu.sqj.ui.fragment.FragmentMsgList;
import com.huaxixingfu.sqj.ui.fragment.FragmentMy;
import com.huaxixingfu.sqj.utils.AppLogMessageMgr;
import com.huaxixingfu.sqj.utils.NetWorkUtils;
import com.huaxixingfu.sqj.utils.SPManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.liteav.debug.GenerateTestUserSig;
import com.tencent.qcloud.tuicore.TUILogin;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 *    desc   : ????????????
 */
public final class HomeActivity extends AppActivity
        implements NavigationAdapter.OnNavigationListener ,
        WebSocketManager.ConnectStateListener {

    private static final String INTENT_KEY_IN_FRAGMENT_INDEX = "fragmentIndex";
    public static final String INTENT_KEY_IN_FRAGMENT_CLASS = "fragmentClass";

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

        //???????????????????????????
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkConnectChangedReceiver, intentFilter);
    }

    long userId;
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

        if (SPManager.instance(getContext()).isLogin()){
            publishPushToken();
        }

        checkUpdate();
    }

    private void publishPushToken(){
//        ?????????????????????????????? ?????????????????????????????????token??????????????????
//        038c2aaa828ba557d65bc813b186c6ce1716
        XGPushManager.registerPush(AppApplication.getContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object token, int i) {
                AppLogMessageMgr.d("lsm-TPush", "?????????????????????token??????" + token);
                HashMap<String, Object> map = new HashMap<>();
                map.put("thirdType","32");
                map.put("accessToken", token);
                EasyHttp.post(HomeActivity.this).json(map).api(new PushTokenBind())
                        .request(new OnHttpListener<HttpData>() {
                            @Override
                            public void onSucceed(HttpData result) {
                                AppLogMessageMgr.e("token???????????????" + result.getMessage());
                            }

                            @Override
                            public void onFail(Exception e) {
                                AppLogMessageMgr.e(e.getMessage());
                            }
                        });
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                AppLogMessageMgr.d("lsm-TPush", "???????????????????????????" + errCode + ",???????????????" + msg);

            }
        });
    }


    private void checkUpdate(){
        HashMap<String, Object> map= new HashMap<>();
        map.put("appVersionNum", BuildConfig.VERSION_CODE);
        EasyHttp.post(this)
                .api(new AppVersionApi())
                .json(map)
                .request(new OnHttpListener<HttpData<AppVersionApi.Bean>>() {
                    @Override
                    public void onSucceed(HttpData<AppVersionApi.Bean> result) {
                        if (result.getData() != null){
                            showUpdateDialog(result.getData());
                        }else {
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_PATH);
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_VERSION);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
    }

    private void showUpdateDialog(AppVersionApi.Bean data){
        String savePath = SPManager.instance(this).getString(Constants.UPDATE_PATH);
        String saveVersion = SPManager.instance(this).getString(Constants.UPDATE_VERSION);
        File file = null;
        if (!TextUtils.isEmpty(saveVersion) && saveVersion.equals(data.appVersionNum)){
            if (!TextUtils.isEmpty(savePath)){
                file = new File(savePath);
            }
        }

        new UpdateDialog.Builder(this)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setUpdateLog(data.appVersionContent)
                .setVersionName(data.appVersionNum)
                .setUpdateTitle(data.appVersionTitle)
                .setForceUpdate(data.appVersionUpdateFlag != 0)
                .setOnCompleteListener(new UpdateDialog.OnCompleteListener() {
                    @Override
                    public void onComplete(File file, String url) {
                        SPManager.instance(getContext()).put(Constants.UPDATE_VERSION, data.appVersionNum);
                        SPManager.instance(getContext()).put(Constants.UPDATE_PATH, file.getAbsolutePath());
                    }
                })
                .setExistsApkFile(file)
                .show();
    }




    private void initIM() {
        if (SPManager.instance(getActivity()).isLogin()) {
            userId = SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId;

            WebSocketManager.getInstance().init(SPManager.instance(getApplicationContext()).getToken(), this);

            // 1.???????????????
            TUILogin.init(getApplicationContext(), GenerateTestUserSig.SDKAPPID, null,null);

            TUILogin.login(userId+"", GenerateTestUserSig.genTestUserSig(userId+""), null);

        }
    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onConnectFailed() {
        //runOnUiThread(() -> Toast.makeText(getContext(), "IM????????????", Toast.LENGTH_SHORT).show());

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onMessage(String text) {

    }

    public static class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //?????????????????????????????????
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean networkFlag = NetWorkUtils.getInstances().isNetSystemUsable(context);
                WebSocketManager webSocketManager = WebSocketManager.getInstance();
                //????????????????????????websocket ??? ????????????????????????????????? ??? ??????????????????????????????????????????
                if (SPManager.instance(context).isLogin() && !webSocketManager.isConnect() && webSocketManager.isMaxReconnectCount() && networkFlag) {
                    webSocketManager.refreshReconnectCount().reconnect();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //???????????????????????????????????????
        if (!isServiceRunning(ImService.class.getName(), getContext()) && SPManager.instance(getApplicationContext()).isLogin()) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getContext(), ImService.class));
            } else {
                startService(new Intent(getContext(), ImService.class));
            }*/
            startService(new Intent(getContext(), ImService.class));
        }
    }

    public boolean isServiceRunning(String className, Context context) {
        //???????????????
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //??????????????????????????????????????????
        List<android.app.ActivityManager.RunningServiceInfo> runningservice = manager.getRunningServices(1000);
        //??????
        for (android.app.ActivityManager.RunningServiceInfo runningServiceInfo : runningservice) {
            //????????????????????????????????????
            ComponentName service = runningServiceInfo.service;
            //???????????????????????????????????????
            String name = service.getClassName();
            //??????
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
        // ???????????? Fragment ????????????
        outState.putInt(INTENT_KEY_IN_FRAGMENT_INDEX, mViewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // ???????????? Fragment ????????????
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
                if(fragmentIndex == 2){
                    if(SPManager.instance(getContext()).isLogin()){
                        if(SPManager.instance(AppApplication.getInstances()).
                                getModel(Constants.USERINFO, PersonDataBean.class).
                                getUserResidentCertStatus() == 2){
                            mViewPager.setCurrentItem(fragmentIndex);
                            mNavigationAdapter.setSelectedPosition(fragmentIndex);
                        }else{

                            new MessageDialog.Builder(getContext())
                                    .setTitle("????????????")
                                    .setMessage("????????????????????????????????????")
                                    .setCancelable(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {

                                        }
                                    })
                                    .show();

                        }

                    }else{
                        ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
                    }
                }else{

                    mViewPager.setCurrentItem(fragmentIndex);
                    mNavigationAdapter.setSelectedPosition(fragmentIndex);
                }
                break;
            default:
                break;
        }
    }

    /**
     * {@link NavigationAdapter.OnNavigationListener}
     */

    @Override
    public boolean onNavigationItemSelected(int fragmentIndex) {
        switch (fragmentIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
                if(fragmentIndex == 2){
                    if(SPManager.instance(getContext()).isLogin()){
                        if(SPManager.instance(AppApplication.getInstances()).
                                getModel(Constants.USERINFO, PersonDataBean.class).
                                getUserResidentCertStatus() == 2){
                            mViewPager.setCurrentItem(fragmentIndex);
                        }else{

                            new MessageDialog.Builder(getContext())
                                    .setTitle("????????????")
                                    .setMessage("????????????????????????????????????")
                                    .setCancelable(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {

                                        }
                                    })
                                    .show();

                        }

                    }else{
                        ActivityManager.getInstance().loginOut(getContext());
                    }
                }else{

                    mViewPager.setCurrentItem(fragmentIndex);
                }

                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // ???????????????????????????
                .navigationBarColor(R.color.white);
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint);
            return;
        }

        // ???????????????????????????????????????????????????????????????
        moveTaskToBack(false);
        postDelayed(() -> {
            // ?????????????????????????????????????????????
            ActivityManager.getInstance().finishAllActivities();
            // ????????????????????????????????? API ?????????????????? Activity onDestroy ???????????????????????????
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