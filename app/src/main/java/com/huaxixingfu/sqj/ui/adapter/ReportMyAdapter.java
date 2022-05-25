package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.HomeCloumnContentNewsApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;

/**
 *    desc   : 我的举报
 */
public final class ReportMyAdapter extends AppAdapter<HomeCloumnContentNewsApi.Bean.VContentNew> {

    public ReportMyAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView itemTitle,itemState,itemHint,itemContent;

        private ViewHolder() {
            super(R.layout.sqj_item_report_my);
            itemTitle = findViewById(R.id.item_title);
            itemHint = findViewById(R.id.item_hint);
            itemState = findViewById(R.id.item_state);
            itemContent = findViewById(R.id.item_content);
        }

        @Override
        public void onBindView(int position) {
            HomeCloumnContentNewsApi.Bean.VContentNew bean = getItem(position);
            itemTitle.setText(bean.newsTile);
            itemHint.setText(bean.newsSource);
            itemState.setText(bean.createdAt);
            itemHint.setText(bean.createdAt);
            itemContent.setText(bean.createdAt);
        }
    }
}