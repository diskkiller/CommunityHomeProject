package com.huaxixingfu.sqj.http.server;

import androidx.annotation.NonNull;

import com.diskkiller.http.config.IRequestServer;
import com.huaxixingfu.sqj.BuildConfig;

/**
 *    desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer {

    @NonNull
    @Override
    public String getHost() {
        return BuildConfig.HOST_URL;
    }
}