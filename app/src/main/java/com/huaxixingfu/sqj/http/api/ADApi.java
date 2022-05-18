package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 注销账号协议
 */
public final class ADApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CANCEL_AD_DETAIL;
    }

}