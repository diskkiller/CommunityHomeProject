package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 修改密码
 */
public final class PasswordApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FORGET_RESET_PWD;
    }

    public final static class Bean {
        public String dataMessage;
        public boolean status;

        public String getDataMessage() {
            return dataMessage;
        }

        public void setDataMessage(String dataMessage) {
            this.dataMessage = dataMessage;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

}