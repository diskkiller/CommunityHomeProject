package com.diskkiller.http.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.http.model.HttpMethod;

/**
 *    author : diskkiller
 *    time   : 2020/10/07
 *    desc   : Head 请求
 */
public final class HeadRequest extends UrlRequest<HeadRequest> {

    public HeadRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.HEAD.toString();
    }
}