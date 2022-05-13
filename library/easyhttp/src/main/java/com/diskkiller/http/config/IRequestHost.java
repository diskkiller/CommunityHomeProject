package com.diskkiller.http.config;

import androidx.annotation.NonNull;

/**
 *    author : diskkiller
 *    time   : 2019/05/19
 *    desc   : 请求主机配置
 */
public interface IRequestHost {

    /**
     * 主机地址
     */
    @NonNull
    String getHost();
}