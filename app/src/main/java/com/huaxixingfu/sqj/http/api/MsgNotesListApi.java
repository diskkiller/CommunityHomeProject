package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 系统通知列表接口
 */
public final class MsgNotesListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.HOME_NOTICE_MESSAGEPAGE;
    }

}