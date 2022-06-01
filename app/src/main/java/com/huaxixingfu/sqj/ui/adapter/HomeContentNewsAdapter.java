package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.http.listener.OnHttpListener;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.bean.VBanner;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.BannerApi;
import com.huaxixingfu.sqj.http.api.BarListApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.api.NotesListApi;
import com.huaxixingfu.sqj.http.api.NotesSysListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.position.news.SimpleNewListActivity;
import com.huaxixingfu.sqj.ui.fragment.HomeSimpleMF;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 *    desc   : 首页列表
 */
public final class HomeContentNewsAdapter extends AppAdapter<HomeContentNewsApi.Bean.VContentNew> {

    public static final int VIEW_TYPE_HEADER_BANNER = 1;
    public static final int VIEW_TYPE_HEADER_MESSAGE_LIST = 3;
    public static final int VIEW_TYPE_HEADER_LIST = 2;
    public static final int VIEW_TYPE_THEN = 0;
    public static final int VIEW_TYPE_NEW = 4;
    private LifecycleOwner lifecycleOwner;
    private OnHttpListener listener;
    public SimpleMarqueeView marquee;
    public HomeHeaderMessage homeHeaderMessage;

    public HomeContentNewsAdapter(Context context, LifecycleOwner lifecycleOwner, OnHttpListener listener) {
        super(context);
        this.lifecycleOwner =  lifecycleOwner;
        this.listener =  listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER_BANNER) {
            return new HomeHeaderBanner(lifecycleOwner,listener);
        } else if (viewType == VIEW_TYPE_HEADER_MESSAGE_LIST) {
            homeHeaderMessage = new HomeHeaderMessage(lifecycleOwner, listener);
            marquee = homeHeaderMessage.marquee;
            return homeHeaderMessage;
        } else if (viewType == VIEW_TYPE_HEADER_LIST) {
            return new HomeHeaderList(lifecycleOwner, listener);
        } else if (viewType == VIEW_TYPE_THEN) {
            return new HomeHeaderNewTop();
        } else {
            return new ViewNormalHolder();
        }
    }

    public void updateNotify(boolean certification){

        if(homeHeaderMessage != null){
            homeHeaderMessage.updateNotify(certification);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER_BANNER;
        } else if (position == 1) {
            return VIEW_TYPE_HEADER_MESSAGE_LIST;
        } else if (position == 2) {
            return VIEW_TYPE_HEADER_LIST;
        } else if (position == 3) {
            return VIEW_TYPE_THEN;
        } else {
            return VIEW_TYPE_NEW;
        }
    }

    private final class HomeHeaderBanner extends AppAdapter<?>.ViewHolder {

        private Banner banner;
        private LifecycleOwner lifecycleOwner;
        private OnHttpListener listener;

        private HomeHeaderBanner(LifecycleOwner lifecycleOwner,OnHttpListener listener) {
            super(R.layout.sqj_fragment_home_banner_item);
            banner = findViewById(R.id.banner);
            banner.setIndicator(new CircleIndicator(getContext()));

            this.lifecycleOwner = lifecycleOwner;
            this.listener = listener;
        }

        @Override
        public void onBindView(int position) {
            initBanner();
        }

        /**
         * 轮播图片请求并展示
         */
        private void initBanner() {
            EasyHttp.post(lifecycleOwner)
                    .api(new BannerApi())
                    .request(new HttpCallback<HttpData<List<VBanner>>>(listener) {

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

                                                BrowserActivity.start(getContext(),data.appGuideUrl,data.appGuideId);

                                            });
                                        }

                                    });

                                    banner.setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(Object data, int position) {
                                            if(StringUtils.isNotEmpty(models.get(position).appGuideUrl))
                                                BrowserActivity.start(getContext(),models.get(position).appGuideUrl,models.get(position).appGuideId);
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
    }

    private final class HomeHeaderNewTop extends AppAdapter<?>.ViewHolder {



        private HomeHeaderNewTop() {
            super(R.layout.sqj_fragment_home_new_top_item);

        }

        @Override
        public void onBindView(int position) {
            findViewById(R.id.move).setOnClickListener(view->{
                SimpleNewListActivity.start((BaseActivity) getContext(),"通知公告","TONGZHIGONGGAO");
            });
        }
    }

    private final class HomeHeaderMessage extends AppAdapter<?>.ViewHolder {

        private SimpleMarqueeView marquee;

        private LifecycleOwner lifecycleOwner;
        private OnHttpListener listener;

        private HomeHeaderMessage(LifecycleOwner lifecycleOwner,OnHttpListener listener) {
            super(R.layout.sqj_fragment_home_message_item);
            this.lifecycleOwner = lifecycleOwner;
            this.listener = listener;
            marquee = findViewById(R.id.marquee);
        }

        @Override
        public void onBindView(int position) {
            boolean login = SPManager.instance(getContext()).isLogin();
            PersonDataBean model = SPManager.instance(AppApplication.getInstances())
                    .getModel(Constants.USERINFO, PersonDataBean.class);
            boolean resident = model != null && model.getUserResidentCertStatus() == 2;

            if (login && resident){
                updateNotify(true);
            }else {
                updateNotify(false);
            }
        }


        public void updateNotify(boolean certification) {
            if(certification){
                 initNotesList();
            }else{
                initNotesSysList();
            }
        }

        /**
         * 获取社区公告列表
         */
        private void initNotesList() {

            HashMap<String, Object> map = new HashMap<>();
            map.put("size","5");
            map.put("page","0");
            map.put("id","");

            EasyHttp.post(lifecycleOwner)
                    .api(new NotesListApi())
                    .json(map)
                    .request(new HttpCallback<HttpData<List<NotesListApi.Bean>>>(listener) {

                        @Override
                        public void onSucceed(HttpData<List<NotesListApi.Bean>> data) {
                            if(data.getData() != null){
                                List<NotesListApi.Bean> models =  data.getData();
                                susseccControl( models);
                            }
                        }
                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                        }
                    });
        }

        /**
         * 获取系统公告列表
         */
        private void initNotesSysList() {

            HashMap<String, Object> map = new HashMap<>();
            map.put("size","5");
            map.put("page","0");
            map.put("id","");

            EasyHttp.post(lifecycleOwner)
                    .api(new NotesSysListApi())
                    .json(map)
                    .request(new HttpCallback<HttpData<NotesSysListApi.Bean>>(listener) {

                        @Override
                        public void onSucceed(HttpData<NotesSysListApi.Bean> data) {
                            if(data.getData() != null){
                                List<NotesListApi.Bean> models =  data.getData().content;
                                susseccControl( models);
                            }
                        }
                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                        }
                    });
        }


        private void  susseccControl(List<NotesListApi.Bean> models){
            if((null != models) && (models.size()>0)){
                HomeSimpleMF<NotesListApi.Bean> marqueeFactory = new HomeSimpleMF<NotesListApi.Bean>(getContext(), view->{
                    BrowserActivity.start(getContext(),view.detailUrl,view.appGuideId);
                });
                marqueeFactory.setData(models);
                marquee.setMarqueeFactory(marqueeFactory);
                marquee.startFlipping();
            }
        }
    }


    private final class HomeHeaderList extends AppAdapter<?>.ViewHolder {

        private LifecycleOwner lifecycleOwner;
        private OnHttpListener listener;
//        HomeContentPositionAdapter adapter;
        private HomeHeaderList(LifecycleOwner lifecycleOwner,OnHttpListener listener) {
            super(R.layout.sqj_fragment_home_list_item);
            this.lifecycleOwner = lifecycleOwner;
            this.listener = listener;
            initBar();
        }

        /**
         * 金刚位请求并展示
         */
        private void initBar() {
            EasyHttp.post(lifecycleOwner)
                    .api(new BarListApi())
                    .request(new HttpCallback<HttpData<List<List<BarListApi.Bean>>>>(listener) {

                        @Override
                        public void onSucceed(HttpData<List<List<BarListApi.Bean>>> data) {
                            if(data != null && data.getData() != null){
                                List<List<BarListApi.Bean>> lists = data.getData();
                                for (int i = 0; i < lists.size(); i++) {
                                    initRv(lists.get(i), i, lists.size());
                                }
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            super.onFail(e);
                        }
                    });
        }


        public void initRv(List<BarListApi.Bean> list, int index, int size){
            RecyclerView recyclerView = new RecyclerView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(params);
            ViewGroup itemView = (ViewGroup) getItemView();
            if (index == 0){
                itemView.removeAllViews();
            }
            itemView.addView(recyclerView);
            if (index < size-1){
                View view = new View(getContext());
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) getContext().getResources().getDimension(R.dimen.dp_10));
                view.setLayoutParams(params2);
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_fff8f8f8));
                itemView.addView(view);
            }

            HomeContentPositionAdapter adapter = new HomeContentPositionAdapter(getContext(),lifecycleOwner,listener);
            adapter.setData(list);
            LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),4);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }



        @Override
        public void onBindView(int position) {

        }
    }

    public final class ViewNormalHolder extends AppAdapter<?>.ViewHolder {

        private final TextView itemType,itemTitle,itemTime;
        private final ImageView itemImg;

        private ViewNormalHolder() {
            super(R.layout.sqj_fragment_home_new_item);
            itemTitle = findViewById(R.id.item_title);
            itemType = findViewById(R.id.item_type);
            itemTime = findViewById(R.id.item_time);
            itemImg = findViewById(R.id.item_img);
        }

        @Override
        public void onBindView(int position) {
            HomeContentNewsApi.Bean.VContentNew bean = getItem(position);
            itemTitle.setText(bean.newsTile);
            itemType.setText(bean.newsColumnName);
            itemTime.setText(bean.createdAt);
            String url = bean.newsImageUrl;
            if(StringUtils.isNotEmpty(url)){
                itemImg.setVisibility(View.VISIBLE);
                GlideApp.with(getContext())
                        .load(url)
                        .centerCrop()
                        .into(itemImg);
            }else{
                itemImg.setVisibility(View.GONE);
            }
        }
    }
}