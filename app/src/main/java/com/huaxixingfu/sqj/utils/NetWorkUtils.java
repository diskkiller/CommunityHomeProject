package com.huaxixingfu.sqj.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetWorkUtils {

    private static final NetWorkUtils netWorkUtils = new NetWorkUtils();

    public static NetWorkUtils getInstances() {
        return netWorkUtils;
    }

    /**
     * 判断当前网络是否可用(6.0以上版本)
     * 实时
     *
     * @param context
     * @return
     */
    @SuppressLint("InlinedApi")
    public boolean isNetSystemUsable(Context context) {
        boolean isNetUsable = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
        if (networkCapabilities != null) {
            isNetUsable = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return isNetUsable;
    }
}
