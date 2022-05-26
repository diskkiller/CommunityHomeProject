package com.huaxixingfu.sqj.ui.activity.position.news;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.huaxixingfu.sqj.aop.Log;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.HomeCloumnContentNewsApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.api.NewsColumnApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.CertificationActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.HomeContentNewsAdapter;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *  新闻列表
 */
public class SimpleNewListActivity extends AppActivity {


    private SimpleNewsAdapter adapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView recycler;
    int page = 0;

    public static  final String TITLE_KEY = "TITLE_KEY";
    public static  final String TITLE_REQUEST = "TITLE_REQUEST";

    @CheckNet
//    @Log
    public static void start(BaseActivity activity, String title, String content) {
        Intent intent = new Intent(activity, SimpleNewListActivity.class);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra(TITLE_REQUEST, content);
        activity.startActivity(intent);
    }

    private String requestKey = "";

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_simple_news_list_layout;
    }

    @Override
    protected void initView() {

        String title = getString(TITLE_KEY);
        requestKey = getString(TITLE_REQUEST);

        if(StringUtils.isEmpty(requestKey)){
            requestKey = "";
        }
        TitleBar tb_title = findViewById(R.id.tb_title);
        if(StringUtils.isEmpty(title)){
            tb_title.setTitle(getString(R.string.news_simple_list_title));
        }else{
            tb_title.setTitle(title);
        }

        initRv();
    }

    private void initRv(){
        recycler = findViewById(R.id.recycler);
        mRefreshLayout = findViewById(R.id.rl_refresh);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initHomeContentNews(true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                initHomeContentNews(false);
            }
        });
        mRefreshLayout.setEnableLoadMore(true);
        adapter = new SimpleNewsAdapter(getContext());
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                HomeContentNewsApi.Bean.VContentNew model = adapter.getData().get(position);
                BrowserActivity.start(getContext(),model.newsUrl,model.newsId);
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        page = 0;
        initHomeContentNews(false);
    }


    /**
     * 头像请求并展示
     */
    private void initHomeContentNews(boolean isLoadMore) {


        HashMap<String, Object> map = new HashMap<>();
        map.put("size","10");
        map.put("page",page);
        map.put("newsColumnCode",requestKey);
        EasyHttp.post(this)
                .api(new HomeCloumnContentNewsApi())
                .json(map)
                .request(new HttpCallback<HttpData<HomeContentNewsApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<HomeContentNewsApi.Bean> data) {
                        if(data.getData() != null){
                            HomeContentNewsApi.Bean model = data.getData();
                            if(null != model){
                                List<HomeContentNewsApi.Bean.VContentNew> news = model.content;
                                if((null != news) && (news.size()>0)){

                                    if(isLoadMore){

                                        adapter.addData(news);
                                        mRefreshLayout.finishLoadMore();

                                    }else{
                                        adapter.clearData();
                                        adapter.setData(news);
                                        mRefreshLayout.finishRefresh();
                                    }

                                }else {
                                    mRefreshLayout.finishLoadMore();
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
