package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 用户注册
 */
public final class RegisterApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REGISTER;
    }

    public final static class Bean {
        public String userId;//: 0,
        public boolean status;//: true,
        public String message;//: "string"

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getDataMessage() {
            return message;
        }

        public void setDataMessage(String message) {
            this.message = message;
        }
    }
}