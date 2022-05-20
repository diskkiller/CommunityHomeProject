package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 查看好友信息
 */
public final class ChatFriendDetailApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_FRIEND_DETAIL;
    }

}