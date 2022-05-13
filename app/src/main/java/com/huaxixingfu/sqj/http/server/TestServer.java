package com.huaxixingfu.sqj.http.server;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.BuildConfig;

/**
 *    desc   : 测试环境
 */
public class TestServer extends ReleaseServer {

    @NonNull
    @Override
    public String getHost() {
        return BuildConfig.HOST_URL;
    }
}