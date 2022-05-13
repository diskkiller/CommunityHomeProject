package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   :
 */
public final class FeedBackApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FEEDBACK_CONTENT;
    }
}