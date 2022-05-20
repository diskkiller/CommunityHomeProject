package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.app.myAppAdapter;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.huaxixingfu.sqj.utils.TimeUtils;

/**
 *    desc   : 聊天列表
 */
public final class MsgListAdapter extends myAppAdapter<Conersation> {

    public MsgListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Conersation conersation) {
        TextView tv_nickname,tv_content,tv_unread,tv_time;
        ImageView niv_avater;
        tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        tv_content = baseViewHolder.getView(R.id.tv_content);
        tv_unread = baseViewHolder.getView(R.id.tv_unread);
        tv_time = baseViewHolder.getView(R.id.tv_time);
        niv_avater = baseViewHolder.getView(R.id.niv_avater);

        Conersation bean = conersation;
        tv_nickname.setText(bean.nickName);
        tv_content.setText(bean.chatBody.content);
        tv_time.setText(TimeUtils.formatTime(bean.chatBody.systemTime, TimeUtils.Time_FORMAT_HM));
        tv_unread.setText(String.valueOf(bean.unreadMsgNum));

        if(bean.unreadMsgNum < 1)
            tv_unread.setVisibility(View.GONE);

        /*if(conersation.chatBody.chatType == 1){

        }else{

        }*/

        GlideApp.with(getContext())
                .load(BuildConfig.HOST_PIC_FILE_URL+bean.chatBody.extras.avatarUrl)
                .into(niv_avater);

    }

}