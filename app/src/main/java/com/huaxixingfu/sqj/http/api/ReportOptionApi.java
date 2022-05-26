package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;
import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 首页-新闻
 */
public final class ReportOptionApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REPORT_TYPE_LIST;
    }

    public final static class ReportItemBean implements Serializable {

        public String name;
        public int code;
        public List<ReportItemBean> children ;
    }
}