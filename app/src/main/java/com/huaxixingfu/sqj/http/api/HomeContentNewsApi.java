package com.huaxixingfu.sqj.http.api;

import androidx.annotation.NonNull;

import com.diskkiller.http.config.IRequestApi;
import com.diskkiller.http.config.IRequestType;
import com.diskkiller.http.model.BodyType;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 首页-新闻
 */
public final class HomeContentNewsApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.HOME_CONTENT_NEW;
    }

    public final static class Bean {
        public int page;
        public int size;
        public int total;
        public boolean first;
        public boolean last;
        public int totalPages;
        public List<VContentNew> content ;

        public final static class VContentNew {
            public int newsId;
            public int newsType;
            public int newsSort;
            public int newsStatus;
            public int newsJumpFlag;
            public int newsJumpType;
            public String newsTile;
            public String newsColumnCode;
            public String newsColumnName;
            public String newsImageUrl;
            public String newsSource;
            public String newsAuthor;
            public String newsJumpUrl;
            public String newsContent;
            public String createdAt;
            public String newsUrl;


        }
    }
}