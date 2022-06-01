package com.huaxixingfu.sqj.ui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.myAppAdapter;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 *    desc   :
 */
public final class GroupMemberAdapter extends BaseQuickAdapter<GroupMemberBean,BaseViewHolder> {

    private boolean isMinus;

    public GroupMemberAdapter() {
        super(R.layout.sqj_item_gride_member);
    }

    public void setMinus(boolean isMinus){
        this.isMinus = isMinus;
    }

    public boolean isMinus() {
        return isMinus;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, GroupMemberBean conersation) {
        NiceImageView niv_avater = baseViewHolder.getView(R.id.iv_user_photo_center);
        if(conersation.nickname.equals("+")){
            niv_avater.setImageDrawable(null);
            niv_avater.setBackgroundResource(R.drawable.smiley_add_selector);
            baseViewHolder.setText(R.id.tv_title,"添加成员");
            baseViewHolder.setGone(R.id.badge_delete,true);
        }else if(conersation.nickname.equals("-")){
            niv_avater.setImageDrawable(null);
            niv_avater.setBackgroundResource(R.drawable.smiley_minus_selector);
            baseViewHolder.setText(R.id.tv_title,"删除成员");
            baseViewHolder.setGone(R.id.badge_delete,true);
        }else{
            if(conersation.isChatGroupUserId){
                baseViewHolder.setGone(R.id.badge_delete,true);
            }else{
                baseViewHolder.setGone(R.id.badge_delete,!isMinus);
            }
            baseViewHolder.setText(R.id.tv_title,conersation.userNickName);
            GlideApp.with(getContext())
                    .load(conersation.residentAvatarUrl)
                    .into(niv_avater);
            baseViewHolder.setGone(R.id.tv_is_owner,!conersation.isChatGroupUserId);
        }


    }

}