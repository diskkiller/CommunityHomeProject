package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 居民认证
 */
public final class SubmitCommunityDataApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.RESIDENT_SUBMIT;
    }
}