package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;

/**
 *    author : diskkiller
 *    desc   : 注销，获取验证码
 */
public final class LogoutCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_SECURITY_LOGOUT_CODE;
    }

    public final static class Bean {
        public boolean status;
        public String message;
        public String code;
    }
}