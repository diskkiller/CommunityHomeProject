package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 编辑
 */
public final class EditGroupFriendNikeNameApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_CHANGE_FRIEND_NICKNAME;
    }
}