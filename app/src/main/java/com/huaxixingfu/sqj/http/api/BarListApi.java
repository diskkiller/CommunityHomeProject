package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;

/**
 *    author : diskkiller
 *    desc   : 导航栏
 */
public final class BarListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.BAR_LIST;
    }

    public final static class Bean implements Serializable {
//        public int sysBarType;
//        public long appGuideId;
//        public String sysBarTitle;
//        public String appGuideImageUrl;
//        public String sysBarImageUrl;
//        public String sysBarTypeName;
//        public String sysBarUrl;
//        public boolean isLoginFlag;
//        public String newsColumnCode;

        public Integer sysBarId;
        public String sysBarTitle;
        public String sysBarImageUrl;
        public Integer sysBarType;
        public String sysBarTypeName;
        public String sysBarUrl;
        public Boolean isLoginFlag;
        public String sysBarCode;
        public Integer sysBarPosition;
        public String sysBarPositionName;

    }
}