package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 群公告列表接口
 */
public final class GroupNotesListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_NOTICE;
    }

}