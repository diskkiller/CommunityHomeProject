package com.huaxixingfu.sqj.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class GroupAddMemberHorAdapter extends BaseQuickAdapter<MailListApi.Bean,BaseViewHolder> {

    List<MailListApi.Bean> data;

    public GroupAddMemberHorAdapter(@Nullable List<MailListApi.Bean> data) {
        super(R.layout.sqj_item_gride_member, data);
        this.data = data;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MailListApi.Bean contactsListBean) {
        baseViewHolder.setText(R.id.tv_title, contactsListBean.chatFriendNiceName);
        NiceImageView niv_avater = baseViewHolder.getView(R.id.iv_user_photo_center);
        GlideApp.with(getContext())
                .load(contactsListBean.userAvatarUrl)
                .into(niv_avater);
    }

}