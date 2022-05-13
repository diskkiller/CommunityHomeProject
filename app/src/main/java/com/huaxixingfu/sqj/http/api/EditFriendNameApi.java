package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 编辑备注
 */
public final class EditFriendNameApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_FRIEND_EDITE_RENAME;
    }
}