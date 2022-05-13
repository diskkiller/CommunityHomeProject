package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;

/**
 *    author : diskkiller
 *    desc   : 修改密码，获取验证码
 */
public final class ResetPasswordCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_SECURITY_PASSWORD_CODE;
    }
}