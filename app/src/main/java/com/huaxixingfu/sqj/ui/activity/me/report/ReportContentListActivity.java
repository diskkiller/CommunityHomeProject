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
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;
import java.util.List;


/**
 *  选择举报类型
 */
public class ReportContentListActivity extends AppActivity {



    public static  final int  CHATSETTING = 1;
    public static  final int  GROUPSETTING = 2;
    public static  final int  NEWSTYPE = 3;

    private ReportContentListAdapter adapter;
    private RecyclerView recycler;
    int page = 0;

    public static  final String TITLE_KEY = "TITLE_KEY";
    public static  final String TITLE_REQUEST = "TITLE_REQUEST";

    @CheckNet
//    @Log
    public static void start(BaseActivity activity, String title, int content) {
        Intent intent = new Intent(activity, ReportContentListActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(TITLE_REQUEST, content);
        activity.startActivity(intent);
    }

//    @CheckNet
//    @Log
    public static void start(BaseActivity activity) {
       start(activity,"",NEWSTYPE);
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
                ReportOptionApi.Bean1.VContentNew model = adapter.getData().get(position);
                ReportSubmitActivity.start(ReportContentListActivity.this,model.newsTile);
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        page = 0;
        initHomeContentNews();
    }


    /**
     * 头像请求并展示
     */
    private void initHomeContentNews() {


        HashMap<String, Object> map = new HashMap<>();
        map.put("size","10");
        map.put("page",page);
        map.put("newsColumnCode",requestKey);
        EasyHttp.post(this)
                .api(new ReportOptionApi())
                .json(map)
                .request(new HttpCallback<HttpData<ReportOptionApi.Bean1>>(this) {

                    @Override
                    public void onSucceed(HttpData<ReportOptionApi.Bean1> data) {
                        if(data.getData() != null){
                            ReportOptionApi.Bean1 model = data.getData();
                            if(null != model){
                                List<ReportOptionApi.Bean1.VContentNew> news = model.content;
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
