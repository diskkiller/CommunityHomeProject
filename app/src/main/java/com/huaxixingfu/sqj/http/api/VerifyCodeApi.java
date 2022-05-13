package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 验证码校验
 */
public final class VerifyCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return "code/checkout";
    }

    /** 手机号 */
    private String phone;
    /** 验证码 */
    private String code;

    public VerifyCodeApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public VerifyCodeApi setCode(String code) {
        this.code = code;
        return this;
    }
}