package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 我的群详情
 */
public final class GroupMemberDetailApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_FRIEND_LIST;
    }

}