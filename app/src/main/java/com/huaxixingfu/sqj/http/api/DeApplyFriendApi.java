package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 拒绝好友
 */
public final class DeApplyFriendApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REFUSE_FRIEND;
    }

    public final static class Bean {
        public String message;
        public boolean status;
    }
}