package com.huaxixingfu.sqj.ui.activity.me.report;


import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.CheckNet;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.FeedBackImageBean;
import com.huaxixingfu.sqj.http.api.ReportDetailsNewsApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.FeedBackImageAdapter;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.huaxixingfu.sqj.ui.adapter.ReportMyAdapter.STATE_DONE_NOT_2;
import static com.huaxixingfu.sqj.ui.adapter.ReportMyAdapter.STATE_DONE_OK_1;
import static com.huaxixingfu.sqj.ui.adapter.ReportMyAdapter.STATE_WILL_DO_0;

/**
 *  举报 详情 或者 提交结果页
 */
public class ReportSubmitDetailsActivity extends AppActivity{



    public static  final String TITLE_REQUEST = "TITLE_REQUEST";
    public static  final String TITLE_RESULT = "TITLE_RESULT";

    private long selectTypeBeanType;
    private boolean resultType;

    private TextView tv_submit, tv_report_num, tv_report_obj, tv_report_obj_1,tv_report_type, tv_report_time,
        tv_report_state, tv_report_result;
    private View ll_result_line, ll_result_content , ll_result_state ;

    @CheckNet
//    @Log
    public static void start(BaseActivity activity, long content,boolean result) {
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
        selectTypeBeanType = getIntent().getLongExtra(TITLE_REQUEST ,-1);
        resultType = getIntent().getBooleanExtra(TITLE_RESULT,false);
        initEvent();
    }

    public void initData() {
        feedback();
    }

    private void feedback(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("appReportId", selectTypeBeanType);
        EasyHttp.post(this)
                .api(new ReportDetailsNewsApi())
                .json(map)
                .request(new HttpCallback<HttpData<ReportDetailsNewsApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<ReportDetailsNewsApi.Bean> mdata) {

                        if(mdata.getData() != null){
                            ReportDetailsNewsApi.Bean data = mdata.getData();
                            if(null != data) {
                                tv_report_num.setText(String.valueOf(data.appReportId));
                                tv_report_obj.setText(String.valueOf(data.appReportToIdName));
                                tv_report_obj_1.setText(String.valueOf(data.appReportToIdName));
                                tv_report_type.setText(data.appReportTypeName);
                                tv_report_time.setText(getTime(data.createdAt));
                                initRv(data.appReportImages);
                                switch (data.appReportStatus) {
                                    case STATE_WILL_DO_0:
//                                        tv_report_state.setText(R.string.report_act_will_do);
                                        tv_report_state.setText(data.appReportStatusName);
                                        tv_report_state.setTextColor(getColor(R.color.main));
                                        tv_report_result.setText(R.string.report_act_details_result_defualt);
                                        break;
                                    case STATE_DONE_OK_1:
                                    case STATE_DONE_NOT_2:
//                                        tv_report_state.setText(R.string.report_act_done);
                                        tv_report_state.setText(data.appReportStatusName);
                                        tv_report_state.setTextColor(getColor(R.color.color_333333));
                                        tv_report_result.setText(data.appExamineDesc);
                                        break;
                                    default:
                                }
                            }
                        }



                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private String getTime(String time){
        if(StringUtils.isNotEmpty(time) && time.length() >16){
            time = time.substring(0,16).replace("T"," ");
        }
        return time;
    }

    private void initEvent() {
        findViewById(R.id.tv_submit)
                .setOnClickListener(this);

        tv_submit = findViewById(R.id.tv_submit);
        tv_report_num = findViewById(R.id.tv_report_num);
        tv_report_obj = findViewById(R.id.tv_report_obj);
        tv_report_obj_1 = findViewById(R.id.tv_report_obj_1);
        tv_report_type = findViewById(R.id.tv_report_type);
        tv_report_time = findViewById(R.id.tv_report_time);
        tv_report_state = findViewById(R.id.tv_report_state);
        tv_report_result = findViewById(R.id.tv_report_result);
        ll_result_line = findViewById(R.id.ll_result_line);
        ll_result_content = findViewById(R.id.ll_result_content);
        ll_result_state = findViewById(R.id.ll_result_state);
        tv_report_result.setMovementMethod(ScrollingMovementMethod.getInstance());





        if(resultType){
            tv_submit.setVisibility(View.VISIBLE);
            setTitle(R.string.report_act_details_title_result);


        }else{
            tv_submit.setVisibility(View.INVISIBLE);
            ll_result_line.setVisibility(View.VISIBLE);
            ll_result_content.setVisibility(View.VISIBLE);
            ll_result_state.setVisibility(View.VISIBLE);
        }
    }

    private void initRv(List<String> stringList) {

        if(stringList == null){
            return ;
        }

        findViewById(R.id.ll_image).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_no_image).setVisibility(View.GONE);

        List<FeedBackImageBean> listImage = new ArrayList<>();
        listImage.clear();
        for (int i = 0; i < stringList.size(); i++) {

            FeedBackImageBean feedBackImageBean = new FeedBackImageBean();
            feedBackImageBean.imageUrlHttp = stringList.get(i);
            feedBackImageBean.defualt = FeedBackImageBean.TYPE_IMAGE_HTTP;
            feedBackImageBean.del = false;
            listImage.add(0,feedBackImageBean);
        }

        RecyclerView rc_image = findViewById(R.id.rc_image);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rc_image.setLayoutManager(layoutManager);

        FeedBackImageAdapter imageAdapter = new FeedBackImageAdapter(ReportSubmitDetailsActivity.this,
                listImage, (View view, int position, FeedBackImageBean typeBean) ->{
        });
        rc_image.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
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