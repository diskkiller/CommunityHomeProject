package com.diskkiller.http.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.http.model.HttpMethod;

/**
 *    author : diskkiller
 *    time   : 2020/10/07
 *    desc   : Delete 请求
 *    doc    : Delete 请求该用 Url 还是 Body 来传递参数：
 *             https://stackoverflow.com/questions/299628/is-an-entity-body-allowed-for-an-http-delete-request
 */
public final class DeleteRequest extends UrlRequest<DeleteRequest> {

    public DeleteRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.DELETE.toString();
    }
}