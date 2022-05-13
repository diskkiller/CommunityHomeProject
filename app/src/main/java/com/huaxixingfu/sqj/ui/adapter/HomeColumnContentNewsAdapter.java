package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.HomeCloumnContentNewsApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;

/**
 *    desc   : 新闻数据列表
 */
public final class HomeColumnContentNewsAdapter extends AppAdapter<HomeCloumnContentNewsApi.Bean.VContentNew> {

    public HomeColumnContentNewsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView itemType,itemTitle,itemTime;
        private final ImageView itemImg;

        private ViewHolder() {
            super(R.layout.sqj_fragment_home_new_item);
            itemTitle = findViewById(R.id.item_title);
            itemType = findViewById(R.id.item_type);
            itemTime = findViewById(R.id.item_time);
            itemImg = findViewById(R.id.item_img);
        }

        @Override
        public void onBindView(int position) {
            HomeCloumnContentNewsApi.Bean.VContentNew bean = getItem(position);
            itemTitle.setText(bean.newsTile);
            itemType.setText(bean.newsSource);
            itemTime.setText(bean.createdAt);
            GlideApp.with(getContext())
                    .load(bean.newsImageUrl)
                    .into(itemImg);
        }
    }
}