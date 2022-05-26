package com.huaxixingfu.sqj.ui.activity.me.report;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.CheckNet;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.ReportOptionApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;
import java.util.List;


/**
 *  选择举报类型
 */
public class ReportContentListActivity extends AppActivity {


//    举报类型code 3000-新闻举报类型 4000-私聊举报类型 5000群聊举报类型
    public static  final int  CHATSETTING = 4000;
    public static  final int  GROUPSETTING = 5000;
    public static  final int  NEWSTYPE = 3000;

    private ReportContentListAdapter adapter;
    private RecyclerView recycler;
    int page = 0;

    public static  final String TITLE_KEY = "TITLE_KEY";
    public static  final String TITLE_REQUEST = "TITLE_REQUEST";
    public static  final String TITLE_REQUEST_MODEL = "TITLE_REQUESTreportItemBean";

    ReportOptionApi.ReportItemBean child;
    @CheckNet
//    @Log
    public static void start(BaseActivity activity,  int content) {
        Intent intent = new Intent(activity, ReportContentListActivity.class);
        intent.putExtra(TITLE_REQUEST, content);
        activity.startActivity(intent);
    }

//    @CheckNet
//    @Log
    public static void start(BaseActivity activity, String title, ReportOptionApi.ReportItemBean reportItemBean) {
        Intent intent = new Intent(activity, ReportContentListActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(TITLE_REQUEST_MODEL, reportItemBean);
        activity.startActivity(intent);
    }

    private int requestKey = NEWSTYPE;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_report_option_list;
    }

    @Override
    protected void initView() {

        String title = getString(TITLE_KEY);
        requestKey = getInt(TITLE_REQUEST);

        if(requestKey == 0){
            Object obj = getIntent().getSerializableExtra(TITLE_REQUEST_MODEL);
            if(obj instanceof ReportOptionApi.ReportItemBean){
                child = (ReportOptionApi.ReportItemBean)obj;
            }
        }
        TitleBar tb_title = findViewById(R.id.tb_title);
        if(StringUtils.isEmpty(title)){
            tb_title.setTitle(getString(R.string.report_act_option_title));
        }else{
            tb_title.setTitle(title);
        }

        initRv();
    }

    private void initRv(){
        recycler = findViewById(R.id.recycler);

        adapter = new ReportContentListAdapter(getContext());
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                ReportOptionApi.ReportItemBean model = adapter.getData().get(position);
                if(model != null && (model.children ==null || model.children.size()==0)){
                    ReportSubmitActivity.start(ReportContentListActivity.this,model.code,model.name);
                }else{
                    start(ReportContentListActivity.this,model.name,model);
                }
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        page = 0;
        if(child != null){
            adapter.clearData();
            adapter.setData(child.children);
            adapter.notifyDataSetChanged();
        }else{
            requestReportListType();
        }
    }


    /**
     * 头像请求并展示
     */
    private void requestReportListType() {


        HashMap<String, Object> map = new HashMap<>();
        map.put("dictCode",requestKey);
        EasyHttp.post(this)
                .api(new ReportOptionApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<ReportOptionApi.ReportItemBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<ReportOptionApi.ReportItemBean>> model) {
                        if(model.getData() != null){
                            if(null != model){
                                List<ReportOptionApi.ReportItemBean> news = model.getData();
                                if((null != news) && (news.size()>0)){


                                        adapter.clearData();
                                        adapter.setData(news);
                                }else {
                                    ToastUtils.show("暂无更多数据");

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




    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    protected void initData() {

    }
}
