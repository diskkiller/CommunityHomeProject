package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 实名认证
 */
public final class RealNameSubmitApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_PERSON_REALNAME_SUBMIT;
    }

    public final static class Bean {
        public boolean status;
        public String message;
        public String code;
    }
}