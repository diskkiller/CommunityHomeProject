package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : 提交举报
 *    desc   :
 */
public final class ReportSubmitApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.REPORT_SUBMIT;
    }
}