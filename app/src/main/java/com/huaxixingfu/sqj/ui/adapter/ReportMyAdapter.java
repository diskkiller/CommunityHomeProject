package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.diskkiller.base.BaseActivity;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.http.api.HomeCloumnContentNewsApi;
import com.huaxixingfu.sqj.http.api.ReportListNewsApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.ui.activity.me.report.ReportSubmitDetailsActivity;

/**
 *    desc   : 我的举报
 */
public final class ReportMyAdapter extends AppAdapter<ReportListNewsApi.Bean.VContentReport> {


    //            0-待处理 1-举报成功 2-举报驳回
    public  static  final int STATE_WILL_DO_0 = 0;
    public  static  final int STATE_DONE_OK_1 = 1;
    public  static  final int STATE_DONE_NOT_2 = 2;

    public ReportMyAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {

        private final TextView itemState,itemContent,itemHint;

        private ViewHolder() {
            super(R.layout.sqj_item_report_my);


            itemContent = findViewById(R.id.item_content);
            itemState = findViewById(R.id.item_state);
            itemHint = findViewById(R.id.item_hint);
        }

        @Override
        public void onBindView(int position) {
            ReportListNewsApi.Bean.VContentReport bean = getItem(position);

            //            0-待处理 1-举报成功 2-举报驳回
            switch (bean.appReportStatus){
                case STATE_WILL_DO_0 :
//                    itemState.setText(R.string.report_act_will_do);
                    itemState.setText(bean.appReportStatusName);
                    itemState.setTextColor(getColor(R.color.color_ff999999));
                    itemContent.setVisibility(View.INVISIBLE);
//                    itemHint.setVisibility(View.INVISIBLE);
                    break;
                case STATE_DONE_OK_1 :
                case STATE_DONE_NOT_2 :
//                    itemState.setText(R.string.report_act_done);
                    itemState.setText(bean.appReportStatusName);
                    itemState.setTextColor(getColor(R.color.main));
                    itemContent.setText(bean.appExamineDesc);
                    itemContent.setVisibility(View.VISIBLE);
                    itemHint.setVisibility(View.VISIBLE);
                    break;
                default:

            }

            getItemView().setOnClickListener(view->{

                ReportSubmitDetailsActivity.start((BaseActivity) getContext(),bean.appReportId,false);
            });
        }
    }
}