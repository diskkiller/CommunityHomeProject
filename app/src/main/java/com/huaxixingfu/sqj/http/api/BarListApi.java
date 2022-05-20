package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 导航栏
 */
public final class BarListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.BAR_LIST;
    }

    public final static class Bean {
        public int sysBarType;
        public String sysBarTitle;
        public String appGuideImageUrl;
        public String sysBarImageUrl;
        public String sysBarTypeName;
        public String sysBarUrl;
        public boolean isLoginFlag;
        public String newsColumnCode;
    }
}