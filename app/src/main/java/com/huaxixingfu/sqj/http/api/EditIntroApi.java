package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 编辑性别
 */
public final class EditIntroApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_PERSON_EDIT_INTRODUCE;
    }
}