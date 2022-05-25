package com.huaxixingfu.sqj.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;

import com.diskkiller.http.EasyConfig;
import com.diskkiller.http.config.IRequestServer;
import com.diskkiller.umeng.UmengClient;
import com.hjq.bar.TitleBar;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.Log;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.RequestHandler;
import com.huaxixingfu.sqj.http.server.ReleaseServer;
import com.huaxixingfu.sqj.http.server.TestServer;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.other.AppConfig;
import com.huaxixingfu.sqj.other.CrashHandler;
import com.huaxixingfu.sqj.other.DebugLoggerTree;
import com.huaxixingfu.sqj.other.MaterialHeader;
import com.huaxixingfu.sqj.other.SmartBallPulseFooter;
import com.huaxixingfu.sqj.other.TitleBarStyle;
import com.huaxixingfu.sqj.other.ToastLogInterceptor;
import com.huaxixingfu.sqj.other.ToastStyle;
import com.huaxixingfu.sqj.utils.SPManager;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 *    desc   : 应用入口
 */
public final class AppApplication extends Application {

    private static AppApplication instances;
    private static Context mContext;

    @Log("启动耗时")
    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        mContext = this;
        getAppChannel();//渠道信息
        initSdk(this);
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取本类的实例
     *
     * @return 实例
     */
    public static AppApplication getInstances() {
        return instances;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 清理所有图片内存缓存
        GlideApp.get(this).onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level);
    }

    /**
     * 初始化一些第三方框架
     */
    @SuppressLint("MissingPermission")
    public static void initSdk(Application application) {
        // 设置标题栏初始化器
        TitleBar.setDefaultStyle(new TitleBarStyle());

        // 设置全局的 Header 构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((cx, layout) ->
                new MaterialHeader(application).setColorSchemeColors(ContextCompat.getColor(application, R.color.common_accent_color)));
        // 设置全局的 Footer 构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((cx, layout) -> new SmartBallPulseFooter(application));
        // 设置全局初始化器
        SmartRefreshLayout.setDefaultRefreshInitializer((cx, layout) -> {
            // 刷新头部是否跟随内容偏移
            layout.setEnableHeaderTranslationContent(true)
                    // 刷新尾部是否跟随内容偏移
                    .setEnableFooterTranslationContent(true)
                    // 加载更多是否跟随内容偏移
                    .setEnableFooterFollowWhenNoMoreData(true)
                    // 内容不满一页时是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(false)
                    // 仿苹果越界效果开关
                    .setEnableOverScrollDrag(false);
        });

        // 初始化吐司
        ToastUtils.init(application, new ToastStyle());
        // 设置调试模式
        ToastUtils.setDebugMode(AppConfig.isDebug());
        // 设置 Toast 拦截器
        ToastUtils.setInterceptor(new ToastLogInterceptor());

        // 本地异常捕捉
        CrashHandler.register(application);

        // 友盟统计、登录、分享 SDK
        UmengClient.init(application, AppConfig.isLogEnable());

        // Bugly 异常捕捉
        CrashReport.initCrashReport(application, AppConfig.getBuglyId(), AppConfig.isDebug());

        // Activity 栈管理初始化
        ActivityManager.getInstance().init(application);

        // MMKV 初始化
        MMKV.initialize(application);


        // 网络请求框架初始化
        IRequestServer server;
        if (BuildConfig.DEBUG) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                .setLogEnabled(true)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addHeader("Authorization", SPManager.instance(application).getToken())
                // 添加全局请求头
                .addHeader("channel", "sqj_share")
                .addHeader("versionCode", String.valueOf(AppConfig.getVersionCode()))
                .addHeader("model", Build.BRAND + "_" + Build.MODEL)
                .into();


        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback((typeToken, fieldName, jsonToken) -> {
            // 上报到 Bugly 错误列表
            CrashReport.postCatchedException(new IllegalArgumentException(
                    "类型解析异常：" + typeToken + "#" + fieldName + "，后台返回的类型为：" + jsonToken));
        });

        // 初始化日志打印
        if (AppConfig.isLogEnable()) {
            Timber.plant(new DebugLoggerTree());
        }

        // 注册网络状态变化监听
        ConnectivityManager connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager.class);
        if (connectivityManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(@NonNull Network network) {
                    Activity topActivity = ActivityManager.getInstance().getTopActivity();
                    if (!(topActivity instanceof LifecycleOwner)) {
                        return;
                    }

                    LifecycleOwner lifecycleOwner = ((LifecycleOwner) topActivity);
                    if (lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                        return;
                    }

                    ToastUtils.show(R.string.common_network_error);
                }
            });
        }

        // TPNS推送初始化
        XGPushConfig.enableDebug(application, BuildConfig.DEBUG);

    }

    //渠道名称
    private static String appChannel;
    /**
     * 获取channel值
     */
    public String getAppChannel() {
        PackageManager packageManager = this.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null) {
                appChannel = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                android.util.Log.e("aaaaaaappChannel", appChannel);
                return appChannel;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}