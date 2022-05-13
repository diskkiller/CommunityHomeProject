package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 性别
 */
public final class PersonalSexApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_PERSON_QRY_SEX;
    }
}