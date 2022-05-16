package com.huaxixingfu.sqj.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.VNotes;
import com.huaxixingfu.sqj.http.api.FriendListApi;
import com.huaxixingfu.sqj.http.api.msg.ResidentListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.shehuan.niv.NiceImageView;

import org.jetbrains.annotations.NotNull;

/**
 */

public class ResidentListAdapter extends BaseQuickAdapter<ResidentListApi.Bean, BaseViewHolder> {
    public ResidentListAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 清空当前数据
     */
    public void clearData() {
        if (getData() == null || getData().size() == 0) {
            return;
        }

        getData().clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ResidentListApi.Bean model) {
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView tv_content = baseViewHolder.getView(R.id.tv_content);
        NiceImageView niv_avater = baseViewHolder.getView(R.id.niv_avater);

        tv_nickname.setText(model.getResidentNickName());
        tv_content.setText(model.getZoneRoomAddr());

        /*boolean isSelect = model.isSelect;
        ImageView iv_select = baseViewHolder.getView(R.id.iv_select);
        if (isSelect) {
            iv_select.setImageResource(R.mipmap.share_to_family_selected);
        } else {
            iv_select.setImageResource(R.mipmap.share_to_family_unselected);
        }*/

        GlideApp.with(getContext())
                .load(model.getResidentAvatarUrl())
                .into(niv_avater);

    }
}
