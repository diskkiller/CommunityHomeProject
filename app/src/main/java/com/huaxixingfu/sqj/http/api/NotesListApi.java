package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 首页-公告
 */
public final class NotesListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.NOTICE_LIST;
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
        public String detailUrl;
    }
}