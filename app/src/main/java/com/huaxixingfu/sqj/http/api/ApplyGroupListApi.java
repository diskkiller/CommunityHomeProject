package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 我加入的的群组列表接口
 */
public final class ApplyGroupListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_APPLY_LIST;
    }

}