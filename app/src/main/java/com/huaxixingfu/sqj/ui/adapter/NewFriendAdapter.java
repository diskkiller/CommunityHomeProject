package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.FriendListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.huaxixingfu.sqj.utils.TimeUtils;

/**
 *    desc   : 新的好友
 */
public final class NewFriendAdapter extends AppAdapter<FriendListApi.Bean> {

    public NewFriendAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView tv_nickname,tv_content;
        private final ImageView niv_avater;

        private ViewHolder() {
            super(R.layout.sqj_item_new_friend);
            tv_nickname = findViewById(R.id.tv_nickname);
            tv_content = findViewById(R.id.tv_content);
            niv_avater = findViewById(R.id.niv_avater);
        }

        @Override
        public void onBindView(int position) {
            FriendListApi.Bean bean = getItem(position);
            tv_nickname.setText(bean.userNickName);
            tv_content.setText(bean.chatFriendApplyRemark);

            GlideApp.with(getContext())
                    .load(bean.userAvatarUrl)
                    .into(niv_avater);
        }
    }
}