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
import com.huaxixingfu.sqj.http.api.ReportListNewsApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.report.MyReportActivity;
import com.huaxixingfu.sqj.ui.activity.me.report.ReportSubmitDetailsActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.ReportMyAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

/**
 * 功能：我的举报
 */

public class ReportMeFragment extends AppFragment<MyReportActivity>
        implements OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener{

    private ReportMyAdapter adapter;

    private SmartRefreshLayout mRefreshLayout;
    private WrapRecyclerView mRecyclerView;


    int page = 0;

    public static ReportMeFragment newInstance() {
        ReportMeFragment fragment = new ReportMeFragment();
        Bundle args = new Bundle();
//        args.putString("newsColumnCode", newsColumnCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_report_my;
    }

    public void initView() {

        mRefreshLayout = findViewById(R.id.rl_refresh);
        mRecyclerView = findViewById(R.id.rv_list);

        adapter = new ReportMyAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    public void initData() {
        getHomeContentNews(
                10,
                page,false);
    }

    public void getHomeContentNews(int size,int page,boolean isLoadMore){
        HashMap<String, Object> map = new HashMap<>();
        map.put("size",size);
        map.put("page",page);
        EasyHttp.post(this)
                .api(new ReportListNewsApi())
                .json(map)
                .request(new HttpCallback<HttpData<ReportListNewsApi.Bean>>(this) {
                    @Override
                    public void onSucceed(HttpData<ReportListNewsApi.Bean> data) {
                        if(data.getData() != null){
                            ReportListNewsApi.Bean model = data.getData();
                            if (null != model) {
                                List<ReportListNewsApi.Bean.VContentReport> news = model.content;
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
        ReportListNewsApi.Bean.VContentReport model = adapter.getItem(position);
        ReportSubmitDetailsActivity.start(getAttachActivity(),model.appReportId,false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getHomeContentNews(
                10,
                page,true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        getHomeContentNews(
                10,
                page,false);
    }
}
