package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   :
 */
public final class GroupLogoutApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_LOGOUT;
    }

}