package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.File;

/**
 *    author : diskkiller
 *    desc   : 上传图片
 */
public final class UpdateImageApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.COMMON_IMAGE_UPLOAD;
    }

    /** 图片文件 */
    private File file;

    public UpdateImageApi setImage(File file) {
        this.file = file;
        return this;
    }
    public final static class Bean{
        public String httpHost;
        public String imageUri;
    }
}