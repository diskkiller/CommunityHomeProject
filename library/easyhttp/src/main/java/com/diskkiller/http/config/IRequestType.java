package com.diskkiller.http.config;

import androidx.annotation.NonNull;

import com.diskkiller.http.model.BodyType;

/**
 *    author : diskkiller
 *    time   : 2020/01/01
 *    desc   : 请求接口配置
 */
public interface IRequestType {

    /**
     * 获取参数的提交类型
     */
    @NonNull
    BodyType getBodyType();
}