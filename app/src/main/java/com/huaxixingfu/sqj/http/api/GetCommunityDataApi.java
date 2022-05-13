package com.huaxixingfu.sqj.http.api;

import androidx.annotation.NonNull;

import com.diskkiller.http.config.IRequestApi;
import com.diskkiller.http.config.IRequestType;
import com.diskkiller.http.model.BodyType;

/**
 *    author : diskkiller
 *    desc   : 居民认证
 */
public final class GetCommunityDataApi implements IRequestApi , IRequestType {

    @Override
    public String getApi() {
        return API.RESIDENT_COMMUNITY;
    }

    @NonNull
    @Override
    public BodyType getBodyType() {
        return BodyType.JSON;
    }
}