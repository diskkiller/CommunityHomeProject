package com.diskkiller.http.config;

import androidx.annotation.NonNull;

import com.diskkiller.http.EasyConfig;

import okhttp3.OkHttpClient;

/**
 *    author : diskkiller
 *    time   : 2021/03/02
 *    desc   : OkHttpClient 配置
 */
public interface IRequestClient {

    /**
     * 获取 OkHttpClient
     */
    @NonNull
    default OkHttpClient getOkHttpClient() {
        return EasyConfig.getInstance().getClient();
    }
}