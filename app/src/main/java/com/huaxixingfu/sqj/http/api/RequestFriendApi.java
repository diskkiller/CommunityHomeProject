package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 申请好友
 */
public final class RequestFriendApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.SUBMIT_FRIEND;
    }

    public final static class Bean {
        public String message;
        public boolean status;
    }
}