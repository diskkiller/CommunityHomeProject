package com.huaxixingfu.sqj.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.GroupListBean;
import com.huaxixingfu.sqj.http.api.MailListApi;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class GroupListAdapter extends BaseQuickAdapter<GroupListBean,BaseViewHolder> {

    List<GroupListBean> data;
    private LinearLayoutManager mLayoutManager;
    private boolean isApply;

    public GroupListAdapter(@Nullable List<GroupListBean> data,boolean isApply) {
        super(R.layout.sqj_item_group, data);
        this.data = data;
        this.isApply = isApply;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, GroupListBean contactsListBean) {
        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.chatGroupNiceName);
        if (isApply) {
            baseViewHolder.setGone(R.id.ll_apply,false);
        } else {
            baseViewHolder.setGone(R.id.ll_apply,true);
        }
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

}