package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 修改密码
 */
public final class ResetPasswordApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_SECURITY_PASSWORD_SUBMIT;
    }
}