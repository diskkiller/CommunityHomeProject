package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 隐私协议
 */
public final class PrivateApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.PRIVACY_DETAIL;
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

        public int getAppGuideId() {
            return appGuideId;
        }

        public void setAppGuideId(int appGuideId) {
            this.appGuideId = appGuideId;
        }

        public String getAppGuideTitle() {
            return appGuideTitle;
        }

        public void setAppGuideTitle(String appGuideTitle) {
            this.appGuideTitle = appGuideTitle;
        }

        public String getAppGuideContent() {
            return appGuideContent;
        }

        public void setAppGuideContent(String appGuideContent) {
            this.appGuideContent = appGuideContent;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAppGuideStatus() {
            return appGuideStatus;
        }

        public void setAppGuideStatus(int appGuideStatus) {
            this.appGuideStatus = appGuideStatus;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAppGuideScanNum() {
            return appGuideScanNum;
        }

        public void setAppGuideScanNum(int appGuideScanNum) {
            this.appGuideScanNum = appGuideScanNum;
        }

        public String getModifiedAt() {
            return modifiedAt;
        }

        public void setModifiedAt(String modifiedAt) {
            this.modifiedAt = modifiedAt;
        }

        public String getModifiedAtString() {
            return modifiedAtString;
        }

        public void setModifiedAtString(String modifiedAtString) {
            this.modifiedAtString = modifiedAtString;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }
    }
}