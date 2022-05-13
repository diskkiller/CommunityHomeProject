package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 系统通知列表状态更新接口
 */
public final class MsgNotesListEditeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.HOME_NOTICE_MESSAGEPAGE_EDITE;
    }

}