package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 居民认证
 */
public final class GetFeedBackTypeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FEEDBACK_TYPE;
    }
}