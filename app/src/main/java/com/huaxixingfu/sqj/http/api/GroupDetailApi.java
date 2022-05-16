package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 我的群详情
 */
public final class GroupDetailApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_DETAIL;
    }

}