package com.huaxixingfu.sqj.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.GroupListBean;
import com.huaxixingfu.sqj.bean.HomeCenterBean;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 *    desc   :
 */
public final class HomeCenterGroupListAdapter extends BaseQuickAdapter<HomeCenterBean.ChatRoomVOList,BaseViewHolder> {

    List<HomeCenterBean.ChatRoomVOList> data;
    private LinearLayoutManager mLayoutManager;
    private boolean isApply;

    public HomeCenterGroupListAdapter(@Nullable List<HomeCenterBean.ChatRoomVOList> data, boolean isApply) {
        super(R.layout.sqj_item_home_center_group, data);
        this.data = data;
        this.isApply = isApply;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, HomeCenterBean.ChatRoomVOList contactsListBean) {
        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.chatRoomName);
        NiceImageView niv_avater = baseViewHolder.getView(R.id.niv_avater);
        if (isApply) {

        } else {

        }


        GlideApp.with(getContext())
                .load(BuildConfig.HOST_PIC_FILE_URL+contactsListBean.chatRoomAvatar)
                .into(niv_avater);
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

}