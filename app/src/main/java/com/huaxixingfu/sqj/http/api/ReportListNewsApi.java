package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 举报列表
 */
public final class ReportListNewsApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REPORT_MY_LIST;
    }

    public final static class Bean {
        public int page;
        public int size;
        public int total;
        public boolean first;
        public boolean last;
        public int totalPages;
        public List<VContentReport> content ;

        public final static class VContentReport {
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

        }
    }
}