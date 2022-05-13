package com.huaxixingfu.sqj.app;

import com.diskkiller.base.BaseFragment;
import com.huaxixingfu.sqj.action.ToastAction;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.diskkiller.http.listener.OnHttpListener;

import okhttp3.Call;

/**
 *    author : diskkiller
 *    time   : 2018/10/18
 *    desc   : Fragment 业务基类
 */
public abstract class AppFragment<A extends AppActivity> extends BaseFragment<A>
        implements ToastAction, OnHttpListener<Object> {

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return false;
        }
        return activity.isShowDialog();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.showDialog();
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        A activity = getAttachActivity();
        if (activity == null) {
            return;
        }
        activity.hideDialog();
    }

    /**
     * {@link OnHttpListener}
     */

    @Override
    public void onStart(Call call) {
        showDialog();
    }

    @Override
    public void onSucceed(Object result) {
        if (!(result instanceof HttpData)) {
            return;
        }
        toast(((HttpData<?>) result).getMessage());
    }

    @Override
    public void onFail(Exception e) {
        toast(e.getMessage());
    }

    @Override
    public void onEnd(Call call) {
        hideDialog();
    }
}