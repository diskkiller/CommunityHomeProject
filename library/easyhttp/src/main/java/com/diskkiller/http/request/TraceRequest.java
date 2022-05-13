package com.diskkiller.http.request;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.http.model.HttpMethod;

/**
 *    author : diskkiller
 *    time   : 2021/04/29
 *    desc   : Trace 请求
 */
public final class TraceRequest extends UrlRequest<TraceRequest> {

    public TraceRequest(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @NonNull
    @Override
    public String getRequestMethod() {
        return HttpMethod.TRACE.toString();
    }
}