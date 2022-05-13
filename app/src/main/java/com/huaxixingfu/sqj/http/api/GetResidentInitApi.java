package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 居民认证接口
 */
public final class GetResidentInitApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.RESIDENT_INIT;
    }
}