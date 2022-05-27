package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import androidx.annotation.NonNull;

/**
 * Created by lsm on 2022/5/26.
 * Describe:
 */
public final class PushTokenBind implements IRequestApi {
    @NonNull
    @Override
    public String getApi() {
        return API.PUSH_TOKEN_BIND;
    }
}
