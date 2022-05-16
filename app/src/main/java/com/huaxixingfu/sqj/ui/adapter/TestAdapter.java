package com.huaxixingfu.sqj.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.http.api.MailListApi;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class TestAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    List<String> data;

    public TestAdapter(@Nullable List<String> data) {
        super(R.layout.status_item, data);
        this.data = data;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String str) {
        baseViewHolder.setText(R.id.tv_status_text, str);

    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
    }
}