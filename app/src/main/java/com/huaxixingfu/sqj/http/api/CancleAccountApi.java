package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 注销账号协议
 */
public final class CancleAccountApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CANCEL_ACCOUNT_DETAIL;
    }

    public final static class Bean {
        public int appGuideId;
        public String appGuideTitle;
        public String appGuideContent;
        public String userName;
        public int appGuideStatus;
        public int status;
        public int appGuideScanNum;
        public String modifiedAt;
        public String modifiedAtString;
        public String detailUrl;
    }
}