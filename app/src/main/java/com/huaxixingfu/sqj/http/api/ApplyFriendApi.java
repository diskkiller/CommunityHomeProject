package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 同意好友
 */
public final class ApplyFriendApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.APPROVE_FRIEND;
    }

    public final static class Bean {
        public String message;
        public boolean status;
    }
}