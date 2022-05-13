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
        public String status;//: true,
        public String dataMessage;//: "string"

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDataMessage() {
            return dataMessage;
        }

        public void setDataMessage(String dataMessage) {
            this.dataMessage = dataMessage;
        }
    }
}