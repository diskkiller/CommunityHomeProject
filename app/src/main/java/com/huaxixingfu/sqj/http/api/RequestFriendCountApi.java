package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 申请好友数量
 */
public final class RequestFriendCountApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FRIEND_APPLY_COUNT;
    }

    public final static class Bean {
        public Long count;
    }
}