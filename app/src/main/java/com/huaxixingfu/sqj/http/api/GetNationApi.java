package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 编辑
 */
public final class GetNationApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_PERSON_QRY_NATION;
    }
}