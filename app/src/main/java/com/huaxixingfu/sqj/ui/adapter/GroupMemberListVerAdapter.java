package com.huaxixingfu.sqj.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 *    desc   : 群成员列表
 */
public final class GroupMemberListVerAdapter extends BaseQuickAdapter<GroupMemberBean,BaseViewHolder> {

    List<GroupMemberBean> data;
    private LinearLayoutManager mLayoutManager;

    public GroupMemberListVerAdapter(@Nullable List<GroupMemberBean> data) {
        super(R.layout.sqj_item_add_member_group, data);
        this.data = data;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, GroupMemberBean contactsListBean) {

        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.nickname);
        boolean isSelect = contactsListBean.isSelect;
        ImageView iv_select = baseViewHolder.getView(R.id.iv_select);
        if (isSelect) {
            iv_select.setImageResource(R.mipmap.share_to_family_selected);
        } else {
            iv_select.setImageResource(R.mipmap.share_to_family_unselected);
        }

        NiceImageView niv_avater = baseViewHolder.getView(R.id.niv_avater);
        GlideApp.with(getContext())
                .load(contactsListBean.residentAvatarUrl)
                .into(niv_avater);
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

}