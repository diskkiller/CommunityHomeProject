package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 注销
 */
public final class LogoutSubmitApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_SECURITY_LOGOUT_SUBMIT;
    }

    public final static class Bean {
        public boolean status;
        public String message;
        public String code;
    }
}