package com.diskkiller.http.config;

import androidx.annotation.NonNull;

import com.diskkiller.http.model.CacheMode;

/**
 *    author : diskkiller
 *    time   : 2021/05/22
 *    desc   : 请求缓存配置
 */
public interface IRequestCache {

    /**
     * 获取缓存的模式
     */
    @NonNull
    CacheMode getCacheMode();

    /**
     * 获取缓存的有效时长（以毫秒为单位）
     */
    long getCacheTime();
}