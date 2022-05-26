package com.huaxixingfu.sqj.ui.fragment.report;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.WrapRecyclerView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.http.api.HomeCloumnContentNewsApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.report.MyReportActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.HomeColumnContentNewsAdapter;
import com.huaxixingfu.sqj.ui.adapter.ReportRecordeAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

/**
 * 功能：违规记录
 */

public class ReportRecordFragment extends AppFragment<MyReportActivity>
        implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener{

    private ReportRecordeAdapter adapter;

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;

    static String mNewsColumnCode;

    int page = 0;

    public static ReportRecordFragment newInstance(String newsColumnCode) {
        ReportRecordFragment fragment = new ReportRecordFragment();
        Bundle args = new Bundle();
        args.putString("newsColumnCode", newsColumnCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_news_columns;
    }

    public void initView() {

        if (getArguments() != null) {
            mNewsColumnCode = getArguments().getString("newsColumnCode");
        }

        mRefreshLayout = findViewById(R.id.rl_refresh);
        mRecyclerView = findViewById(R.id.rv_list);

        adapter = new ReportRecordeAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    public void initData() {
        getHomeContentNews(
                10,
                page,
                mNewsColumnCode,false);
    }

    public void getHomeContentNews(int size,int page,String newsColumnCode,boolean isLoadMore){
        HashMap<String, Object> map = new HashMap<>();
        map.put("size",size);
        map.put("page",page);
        map.put("newsColumnCode",newsColumnCode);
        EasyHttp.post(this)
                .api(new HomeCloumnContentNewsApi())
                .json(map)
                .request(new HttpCallback<HttpData<HomeCloumnContentNewsApi.Bean>>(this) {
                    @Override
                    public void onSucceed(HttpData<HomeCloumnContentNewsApi.Bean> data) {
                        if(data.getData() != null){
                            HomeCloumnContentNewsApi.Bean model = data.getData();
                            if (null != model) {
                                List<HomeCloumnContentNewsApi.Bean.VContentNew> news = model.content;
                                if ((null != news) && (news.size() > 0)) {

                                    if(isLoadMore){

                                        adapter.addData(news);
                                        mRefreshLayout.finishLoadMore();

                                    }else{
                                        adapter.clearData();
                                        adapter.setData(news);
                                        mRefreshLayout.finishRefresh();
                                    }

                                } else {
                                    if (null != adapter) {
                                        ToastUtils.show("暂无更多数据");
                                    }
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
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        HomeCloumnContentNewsApi.Bean.VContentNew model = adapter.getItem(position);
//        BrowserActivity.start(getActivity(), model.newsUrl);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getHomeContentNews(
                10,
                page,
                mNewsColumnCode,true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        getHomeContentNews(
                10,
                page,
                mNewsColumnCode,false);
    }
}
