package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 手机号验证
 */
public final class LoginPhoneValidateApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.LOGIN_VALIDATE_PHONE;
    }

    public final static class Bean {

        public boolean status;
        public String message;

    }
}