package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   :
 */
public final class DelGroupMemberApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_FRIEND_DEL;
    }

}