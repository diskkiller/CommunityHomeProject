package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 举报详情
 */
public final class ReportDetailsNewsApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REPORT_MY_DETAILS;
    }

    public final static class Bean {

            public int appReportId;
            public int appReportTypeId;
            public String appReportTypeName;
            public String appReportContent;
            public int appReportStatus;
            public String appReportStatusName;
            public String appExamineDesc;
            public String appType;
            public String appTypeName;
            public String createdAt;
            public String appExamineAt;
            public String appReportToIdName;
            public List<String> appReportImages;
            public int appReportToId;

    }
}