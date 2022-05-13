package com.diskkiller.http.config;

import androidx.annotation.NonNull;

import com.diskkiller.http.model.BodyType;
import com.diskkiller.http.model.CacheMode;

/**
 *    author : diskkiller
 *    time   : 2019/05/19
 *    desc   : 请求服务配置
 */
public interface IRequestServer extends
        IRequestHost, IRequestClient,
        IRequestType, IRequestCache {

    @NonNull
    @Override
    default BodyType getBodyType() {
        // 默认以表单的方式提交
        return BodyType.FORM;
    }

    @NonNull
    @Override
    default CacheMode getCacheMode() {
        // 默认的缓存方式
        return CacheMode.DEFAULT;
    }

    @Override
    default long getCacheTime() {
        return Long.MAX_VALUE;
    }
}