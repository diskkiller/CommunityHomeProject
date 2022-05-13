package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 获取验证码
 */
public final class GetForgetCodeApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FORGET_GET_CODE;
    }

    public final static class Bean {

        public boolean status;
        public String message;
        public String code;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}