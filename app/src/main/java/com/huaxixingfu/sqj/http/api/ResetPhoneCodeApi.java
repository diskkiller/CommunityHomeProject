package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 修改手机验证码
 */
public final class ResetPhoneCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_SECURITY_CODE;
    }
}