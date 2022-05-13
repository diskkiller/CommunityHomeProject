package com.huaxixingfu.sqj.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.action.StatusAction;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.widget.StatusLayout;

public class FragmentLife extends TitleBarFragment<AppActivity> implements StatusAction {

    private StatusLayout mStatusLayout;

    public static FragmentLife newInstance() {
        return new FragmentLife();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_life;
    }

    @Override
    protected void initView() {
        mStatusLayout = findViewById(R.id.hl_status_hint);
    }

    @Override
    protected void initData() {
        showEmpty();
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
