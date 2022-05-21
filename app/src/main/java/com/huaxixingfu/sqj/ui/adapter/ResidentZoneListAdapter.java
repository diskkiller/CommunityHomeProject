package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.bean.ZoneResidentBean;
import com.huaxixingfu.sqj.utils.StringUtils;

/**
 *    desc   :
 */
public final class ResidentZoneListAdapter extends AppAdapter<ZoneResidentBean> {

    public ResidentZoneListAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView tv_content_desc,tv_content,tv_chat;
        private final ImageView tv_arrow;

        private ViewHolder() {
            super(R.layout.sqj_item_zone_resident);
            tv_content = findViewById(R.id.tv_content);
            tv_content_desc = findViewById(R.id.tv_content_desc);
            tv_chat = findViewById(R.id.tv_chat);
            tv_arrow = findViewById(R.id.tv_arrow);
        }

        @Override
        public void onBindView(int position) {
            ZoneResidentBean bean = getItem(position);
            if(StringUtils.isNotEmpty(bean.name)){
                tv_content.setText(bean.name);
                tv_arrow.setVisibility(View.VISIBLE);
                tv_chat.setVisibility(View.GONE);
                tv_content_desc.setVisibility(View.INVISIBLE);
            }else{
                tv_content.setText(bean.zoneRoomAddr);
                tv_arrow.setVisibility(View.GONE);
                tv_chat.setVisibility(View.VISIBLE);
                if(bean.isFriend){
                    tv_content_desc.setVisibility(View.VISIBLE);
                    tv_chat.setText("发起聊天");
                }else{
                    tv_content_desc.setVisibility(View.INVISIBLE);
                    tv_chat.setText("申请好友");
                }

            }

        }
    }
}