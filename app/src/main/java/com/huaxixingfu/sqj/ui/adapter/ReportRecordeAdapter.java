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
 *    desc   : 违规记录
 */
public final class ReportRecordeAdapter extends AppAdapter<HomeCloumnContentNewsApi.Bean.VContentNew> {

    public ReportRecordeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView itemType,itemTitle;

        private ViewHolder() {
            super(R.layout.sqj_item_report_recoder);
            itemTitle = findViewById(R.id.item_content);
            itemType = findViewById(R.id.item_title);
        }

        @Override
        public void onBindView(int position) {
            HomeCloumnContentNewsApi.Bean.VContentNew bean = getItem(position);
            itemTitle.setText(bean.newsTile);
            itemType.setText(bean.newsSource);
        }
    }
}