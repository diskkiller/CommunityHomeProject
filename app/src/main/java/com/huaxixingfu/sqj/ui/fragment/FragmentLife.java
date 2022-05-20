package com.huaxixingfu.sqj.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.action.StatusAction;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.position.news.SimpleNewsAdapter;
import com.huaxixingfu.sqj.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

public class FragmentLife extends TitleBarFragment<AppActivity> implements StatusAction {

    private StatusLayout mStatusLayout;

    private SimpleNewsAdapter adapter;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView recycler;
    int page = 0;

    public static FragmentLife newInstance() {
        return new FragmentLife();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_life;
    }

    @Override
    protected void initView() {
        mStatusLayout = findViewById(R.id.hl_status_hint);
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
        adapter = new SimpleNewsAdapter(getContext());
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                HomeContentNewsApi.Bean.VContentNew model = adapter.getData().get(position);
                BrowserActivity.start(getActivity(),model.newsUrl);
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
        map.put("newsColumnCode","SHENGHUOQUAN");
        EasyHttp.post(this)
                .api(new HomeContentNewsApi())
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
    protected void initData() {
        showEmpty();
    }

    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }
}
