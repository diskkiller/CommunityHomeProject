package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 新闻栏目接口
 */
public final class NewsColumnApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.NEWS_COLUMN;
    }

    public final static class Bean {
        public String newsColumnName;
        public String newsColumnCode;
    }
}