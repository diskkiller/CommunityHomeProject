package com.huaxixingfu.sqj.ui.activity.me.report;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.diskkiller.base.BaseActivity;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.CheckNet;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.databinding.SqjActivityReportDetailsBinding;

/**
 *  举报 详情 或者 提交结果页
 */
public class ReportSubmitDetailsActivity extends AppActivity{



    public static  final String TITLE_REQUEST = "TITLE_REQUEST";
    public static  final String TITLE_RESULT = "TITLE_RESULT";

    private String selectTypeBeanType;
    private boolean resultType;

    private TextView tv_submit, tv_report_num, tv_report_obj, tv_report_type, tv_report_time,
        tv_report_state, tv_result_title;
    @CheckNet
//    @Log
    public static void start(BaseActivity activity, String content,boolean result) {
        Intent intent = new Intent(activity, ReportSubmitDetailsActivity.class);
        intent.putExtra(TITLE_REQUEST, content);
        intent.putExtra(TITLE_RESULT, result);
        activity.startActivity(intent);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_report_details;
    }

    @Override
    protected void initView() {
        SqjActivityReportDetailsBinding.inflate(LayoutInflater.from(this));
        selectTypeBeanType = getIntent().getStringExtra(TITLE_REQUEST);
        resultType = getIntent().getBooleanExtra(TITLE_RESULT,false);
        initEvent();
    }

    public void initData() {

    }

    private void initEvent() {
        findViewById(R.id.tv_submit)
                .setOnClickListener(this);

        tv_submit = findViewById(R.id.tv_submit);
        tv_report_num = findViewById(R.id.tv_report_num);
        tv_report_obj = findViewById(R.id.tv_report_obj);
        tv_report_type = findViewById(R.id.tv_report_type);
        tv_report_time = findViewById(R.id.tv_report_time);
        tv_report_state = findViewById(R.id.tv_report_state);
        tv_result_title = findViewById(R.id.tv_result_title);

        if(resultType){
            tv_submit.setVisibility(View.VISIBLE);
            setTitle(R.string.report_act_details_title_result);
        }else{
            tv_submit.setVisibility(View.INVISIBLE);
        }
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_submit) {
            finish();
        }
    }


}