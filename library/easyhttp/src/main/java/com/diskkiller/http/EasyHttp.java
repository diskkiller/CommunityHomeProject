package com.diskkiller.http;

import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.http.request.DeleteRequest;
import com.diskkiller.http.request.DownloadRequest;
import com.diskkiller.http.request.GetRequest;
import com.diskkiller.http.request.HeadRequest;
import com.diskkiller.http.request.OptionsRequest;
import com.diskkiller.http.request.PatchRequest;
import com.diskkiller.http.request.PostRequest;
import com.diskkiller.http.request.PutRequest;
import com.diskkiller.http.request.TraceRequest;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 *    author : diskkiller
 *    time   : 2019/05/19
 *    desc   : 网络请求类
 */
@SuppressWarnings("unused")
public final class EasyHttp {

    /**
     * Get 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static GetRequest get(LifecycleOwner lifecycleOwner) {
        return new GetRequest(lifecycleOwner);
    }

    /**
     * Post 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static PostRequest post(LifecycleOwner lifecycleOwner) {
        return new PostRequest(lifecycleOwner);
    }

    /**
     * Head 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static HeadRequest head(LifecycleOwner lifecycleOwner) {
        return new HeadRequest(lifecycleOwner);
    }

    /**
     * Delete 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static DeleteRequest delete(LifecycleOwner lifecycleOwner) {
        return new DeleteRequest(lifecycleOwner);
    }

    /**
     * Put 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static PutRequest put(LifecycleOwner lifecycleOwner) {
        return new PutRequest(lifecycleOwner);
    }

    /**
     * Patch 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static PatchRequest patch(LifecycleOwner lifecycleOwner) {
        return new PatchRequest(lifecycleOwner);
    }

    /**
     * Options 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static OptionsRequest options(LifecycleOwner lifecycleOwner) {
        return new OptionsRequest(lifecycleOwner);
    }

    /**
     * Trace 请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static TraceRequest trace(LifecycleOwner lifecycleOwner) {
        return new TraceRequest(lifecycleOwner);
    }

    /**
     * 下载请求
     *
     * @param lifecycleOwner      请传入 AppCompatActivity 或者 AndroidX.Fragment 子类
     *                            如需传入其他对象请参考以下两个类
     *                            {@link com.diskkiller.http.lifecycle.ActivityLifecycle}
     *                            {@link com.diskkiller.http.lifecycle.ApplicationLifecycle}
     */
    public static DownloadRequest download(LifecycleOwner lifecycleOwner) {
        return new DownloadRequest(lifecycleOwner);
    }

    /**
     * 根据 TAG 取消请求任务
     */
    public static void cancel(Object tag) {
        cancel(EasyUtils.getObjectTag(tag));
    }

    public static void cancel(String tag) {
        if (tag == null) {
            return;
        }

        OkHttpClient client = EasyConfig.getInstance().getClient();

        // 清除排队等候的任务
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        // 清除正在执行的任务
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 清除所有请求任务
     */
    public static void cancel() {
        OkHttpClient client = EasyConfig.getInstance().getClient();

        // 清除排队等候的任务
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }

        // 清除正在执行的任务
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}