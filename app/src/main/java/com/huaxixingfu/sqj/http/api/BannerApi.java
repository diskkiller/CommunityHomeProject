package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 轮播图
 */
public final class BannerApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.BANNER;
    }

    public final static class Bean {
        public int appGuideId;
        public String appGuideImageUrl;
    }
}