package com.huaxixingfu.sqj.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.WrapRecyclerView;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.bean.VBanner;
import com.huaxixingfu.sqj.bean.VResident;
import com.huaxixingfu.sqj.http.api.BannerApi;
import com.huaxixingfu.sqj.http.api.GetResidentInitApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.api.NotesListApi;
import com.huaxixingfu.sqj.http.api.PersonalDataApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.msg.SystemNotesListActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.adapter.HomeContentNewsAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FragmentHome extends AppFragment<HomeActivity>  {

    private RecyclerView recycler;
    private HomeContentNewsAdapter adapter;
    private SmartRefreshLayout mRefreshLayout;
    int page = 0;
    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_home;
    }

    @Override
    protected void initView() {
        initRv();
        setOnClickListener(R.id.right_icon
        );
        Looper.myQueue().addIdleHandler(()->{
            if(SPManager.instance(getContext()).isLogin()){
                getPersonData();
            }
            return false;
        });
    }

    /**
     *  获取居住信息
     */
    private void  getPersonData(){

        EasyHttp.post(this)
                .api(new PersonalDataApi())
                .request(new HttpCallback<HttpData<PersonDataBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<PersonDataBean> data) {
                        if(data.getData() != null){
                            PersonDataBean personDataBean = data.getData();
                            if("已认证".equals(personDataBean.getCardStartusName())) {
                                getResidentInit();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void getResidentInit(){
        EasyHttp.post(this)
                .api(new GetResidentInitApi())
                .request(new HttpCallback<HttpData<VResident>>(this) {
                    @Override
                    public void onSucceed(HttpData<VResident> mdata) {
                        if(mdata.getData() != null){
                            VResident data = mdata.getData();
                            if(null != data){
                                //0未认证  /1待认证/2认证通过/3认证驳回
                                int residentStatus = data.residentStatus;
                                if(residentStatus == 2){
                                    //2认证通过
                                    ((TextView)findViewById(R.id.tv_title)).setText(data.zoneName);
                                    (findViewById(R.id.left_icon)).setVisibility(View.VISIBLE);
                                    if(adapter != null){
                                        adapter.updateNotify(true);
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

    /**
     * 处理网络请求
     */
    public void initData(){
        initHomeContentNews(false);
    }




    public void initRv(){
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
        adapter = new HomeContentNewsAdapter(getAttachActivity(),this,this);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                HomeContentNewsApi.Bean.VContentNew model = adapter.getData().get(position);
                if(model != null && StringUtils.isNotEmpty(model.newsUrl)){
                    BrowserActivity.start(getActivity(),model.newsUrl);
                }
            }

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }

    /**
     * 头像请求并展示
     */
    private void initHomeContentNews(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size","10");
        map.put("page",page);
        map.put("newsColumnCode","TONGZHIGONGGAO");
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
                                        news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                                        news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                                        news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                                        news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                                        adapter.clearData();
                                        adapter.setData(news);
                                        mRefreshLayout.finishRefresh();
                                    }

                                }else {
                                    mRefreshLayout.finishLoadMore();
                                    //ToastUtils.show("暂无更多数据");

                                }
                            }
                        }

                        if(adapter != null && (adapter.getData() == null || adapter.getData().size() == 0)){
                            List<HomeContentNewsApi.Bean.VContentNew> news = new ArrayList<>();
                            news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                            news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                            news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                            news.add(0,new HomeContentNewsApi.Bean.VContentNew());
                            adapter.clearData();
                            adapter.setData(news);
                            mRefreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.right_icon:
                startActivity(new Intent(getActivity(), SystemNotesListActivity.class));
                break;

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null && adapter.marquee != null){
            adapter.marquee.startFlipping();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null && adapter.marquee != null){
            adapter.marquee.stopFlipping();
        }
    }
}
