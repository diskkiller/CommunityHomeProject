package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;

/**
 *    author : diskkiller
 *    desc   : 实名认证初始化
 */
public final class RealNameApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_PERSON_REALNAME_INIT;
    }

}