package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    APP版本升级
 */
public final class AppVersionApi implements IRequestApi {


    @Override
    public String getApi() {
        return API.APP_VERSION_CHECK;
    }


    public static class Bean {
        public Integer appVersionType;
        public Integer appVersionUpdateFlag;
        public String appVersionNum;
        public String appVersionUrl;
        public String appVersionTitle;
        public String appVersionContent;
        public String appTypeName;
        public String appVersionUpdateFlagName;
        public String createdAt;
        public String modifiedAt;
    }
}