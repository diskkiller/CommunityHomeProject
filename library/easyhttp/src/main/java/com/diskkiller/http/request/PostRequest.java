package com.diskkiller.http.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.http.model.HttpMethod;

/**
 *    author : diskkiller
 *    time   : 2019/07/20
 *    desc   : 主体请求
 */
public final class PostRequest extends BodyRequest<PostRequest> {

    public PostRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.POST.toString();
    }
}