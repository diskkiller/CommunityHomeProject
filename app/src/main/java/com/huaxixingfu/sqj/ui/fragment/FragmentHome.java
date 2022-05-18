package com.huaxixingfu.sqj.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.bean.VBanner;
import com.huaxixingfu.sqj.http.api.BannerApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.api.NotesListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.msg.SystemNotesListActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.home.NewsListActivity;
import com.huaxixingfu.sqj.ui.adapter.HomeContentNewsAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentHome extends AppFragment<HomeActivity> {


    private SimpleMarqueeView marquee;
    private Banner banner;
    private RecyclerView recycler;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_home;
    }

    @Override
    protected void initView() {
        marquee = findViewById(R.id.marquee);
        banner = findViewById(R.id.banner);
        recycler = findViewById(R.id.recycler);
        setOnClickListener(R.id.view_one_img,R.id.view_one_text,
                R.id.view_two_img,R.id.view_two_text,
                R.id.view_three_img,R.id.view_three_text,
                R.id.view_five_img,R.id.view_five_text,
                R.id.view_six_img,R.id.view_six_text,
                R.id.view_seven_img,R.id.view_seven_text,
                R.id.view_eight_img,R.id.view_eight_text,
                R.id.move,R.id.right_icon
        );
    }

    /**
     * 处理网络请求
     */
    public void initData(){
        initBanner();

        initHomeContentNews();

        initNotesList();
    }

    /**
     * 获取公告列表
     */
    private void initNotesList() {
        EasyHttp.post(this)
                .api(new NotesListApi())
                .request(new HttpCallback<HttpData<List<NotesListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<NotesListApi.Bean>> data) {
                        if(data.getData() != null){
                            List<NotesListApi.Bean> models =  data.getData();
                            List<String> datas = new ArrayList<String>();
                            if((null != models) && (models.size()>0)){
                                for (int i=0,j=models.size();i<j;i++){
                                    datas.add(models.get(i).appGuideTitle);
                                }

                                if((null != datas) && (datas.size()>0)){
                                    SimpleMF<String> marqueeFactory = new SimpleMF(getContext());
                                    marqueeFactory.setData(datas);
                                    marquee.setMarqueeFactory(marqueeFactory);
                                    marquee.startFlipping();

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
     * 轮播图片请求并展示
     */
    private void initBanner() {
        EasyHttp.post(this)
                .api(new BannerApi())
                .request(new HttpCallback<HttpData<List<VBanner>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<VBanner>> data) {
                        if(data.getData() != null){
                            List<VBanner> models = data.getData();
                            LogUtil.e("aaaaaaaappGuideImageUrl", "aaaaaaa----" + models);
                            if((null != models) && (models.size()>0)){
                                banner.setAdapter(new BannerImageAdapter<VBanner>(models) {
                                    @Override
                                    public void onBindView(BannerImageHolder holder,
                                                           VBanner data,
                                                           int position,
                                                           int size) {
                                        String img = data.appGuideImageUrl;
                                        LogUtil.e("aaaaaaaappGuideImageUrl", "aaaaaaa----" + img);

                                        GlideApp.with(getContext())
                                                .load(img)
                                                .into(holder.imageView);
                                        holder.imageView.setOnClickListener(view->{

                                            BrowserActivity.start(getActivity(),data.appGuideImageUrl);

                                        });
                                    }

                                });
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
     * 头像请求并展示
     */
    private void initHomeContentNews() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("size","10");
        map.put("page","0");
        map.put("newsColumnCode","");
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
                                    HomeContentNewsAdapter adapter = new HomeContentNewsAdapter(getAttachActivity());
                                    adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                                            HomeContentNewsApi.Bean.VContentNew model = news.get(position);
                                            BrowserActivity.start(getActivity(),model.newsUrl);
                                        }

                                    });
                                    adapter.setData(news);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                                            LinearLayoutManager.VERTICAL,
                                            false);
                                    recycler.setLayoutManager(linearLayoutManager);
                                    recycler.setAdapter(adapter);
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

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_one_img:
            case R.id.view_one_text:
                ToastUtils.debugShow("功能正在书写");
//                startActivity(new Intent(getActivity(), ResidentActivity.class));
                break;
            case R.id.view_two_img:
            case R.id.view_two_text:
                //startActivity(new Intent(getActivity(), NewsListActivity.class));
                break;
            case R.id.view_three_img:
            case R.id.view_three_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.view_four_img:
            case R.id.view_four_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.view_five_img:
            case R.id.view_five_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.view_six_img:
            case R.id.view_six_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.view_seven_img:
            case R.id.view_seven_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.view_eight_img:
            case R.id.view_eight_text:
                ToastUtils.debugShow("功能正在书写");
                break;
            case R.id.move:
                startActivity(new Intent(getActivity(), NewsListActivity.class));
                break;
            case R.id.right_icon:
                startActivity(new Intent(getActivity(), SystemNotesListActivity.class));
                break;

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        marquee.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marquee.stopFlipping();
    }
}
