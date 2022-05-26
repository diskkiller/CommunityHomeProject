package com.huaxixingfu.sqj.ui.activity.me.report;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.ReportOptionApi;

/**
 *    desc   : 选择举报类型 simple列表
 */
public final class ReportContentListAdapter extends AppAdapter<ReportOptionApi.ReportItemBean> {

    public ReportContentListAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewNormalHolder();
    }

    @Override
    public int getItemViewType(int position) {
        return  super.getItemViewType(position);
    }

    private final class ViewNormalHolder extends ViewHolder {

        private final TextView itemTitle;

        private ViewNormalHolder() {
            super(R.layout.sqj_fragment_report_item);
            itemTitle = findViewById(R.id.item_title);

        }

        @Override
        public void onBindView(int position) {
            ReportOptionApi.ReportItemBean bean = getItem(position);
            itemTitle.setText(bean.name);
        }
    }
}