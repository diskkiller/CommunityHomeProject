package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.app.myAppAdapter;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.huaxixingfu.sqj.utils.TimeUtils;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class MaillListAdapter extends BaseQuickAdapter<MailListApi.Bean,BaseViewHolder> {

    List<MailListApi.Bean> data;
    private LinearLayoutManager mLayoutManager;

    public MaillListAdapter(@Nullable List<MailListApi.Bean> data) {
        super(R.layout.sqj_item_maillist, data);
        this.data = data;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MailListApi.Bean contactsListBean) {
        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.chatFriendNiceName);
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    /**
     * 滚动RecyclerView到索引位置
     *
     * @param index
     */
    public void scrollToSection(String index) {
        if (data == null || data.isEmpty()) return;
        if (TextUtils.isEmpty(index)) return;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(index.substring(0, 1), data.get(i).getSection().substring(0, 1))) {
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(i + getHeaderLayoutCount(), 0);
                    return;
                }
            }
        }
    }
}