package com.huaxixingfu.sqj.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.base.action.ClickAction;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.http.listener.OnHttpListener;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppAdapter;
import com.huaxixingfu.sqj.bean.VBanner;
import com.huaxixingfu.sqj.http.api.BannerApi;
import com.huaxixingfu.sqj.http.api.HomeContentNewsApi;
import com.huaxixingfu.sqj.http.api.NotesListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.home.NewsListActivity;
import com.huaxixingfu.sqj.ui.activity.msg.SystemNotesListActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.fragment.HomeSimpleMF;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

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
            HomeHeaderMessage homeHeaderMessage = new HomeHeaderMessage(lifecycleOwner, listener);
            marquee = homeHeaderMessage.marquee;
            return homeHeaderMessage;
        } else if (viewType == VIEW_TYPE_HEADER_LIST) {
            return new HomeHeaderList();
        } else if (viewType == VIEW_TYPE_THEN) {
            return new HomeHeaderNewTop();
        } else {
            return new ViewNormalHolder();
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

                                                BrowserActivity.start(getContext(),data.appGuideUrl);

                                            });
                                        }

                                    });

                                    banner.setOnBannerListener(new OnBannerListener() {
                                        @Override
                                        public void OnBannerClick(Object data, int position) {
                                            if(StringUtils.isNotEmpty(models.get(position).appGuideUrl))
                                                BrowserActivity.start(getContext(),models.get(position).appGuideUrl);
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
                getContext().startActivity(new Intent(getContext(), NewsListActivity.class));
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
            initNotesList();
        }

        /**
         * 获取公告列表
         */
        private void initNotesList() {
            EasyHttp.post(lifecycleOwner)
                    .api(new NotesListApi())
                    .request(new HttpCallback<HttpData<List<NotesListApi.Bean>>>(listener) {

                        @Override
                        public void onSucceed(HttpData<List<NotesListApi.Bean>> data) {
                            if(data.getData() != null){
                                List<NotesListApi.Bean> models =  data.getData();
                                if((null != models) && (models.size()>0)){
                                    HomeSimpleMF<NotesListApi.Bean> marqueeFactory = new HomeSimpleMF<>(getContext(), view->{
                                        BrowserActivity.start(getContext(),view.detailUrl);
                                    });
                                    marqueeFactory.setData(models);
                                    marquee.setMarqueeFactory(marqueeFactory);
                                    marquee.startFlipping();
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

    private final class HomeHeaderList extends AppAdapter<?>.ViewHolder implements ClickAction {



        private HomeHeaderList() {
            super(R.layout.sqj_fragment_home_list_item);

            setOnClickListener(R.id.ll_view_one,
                    R.id.ll_view_two,
                    R.id.ll_view_three,
                    R.id.ll_view_five,
                    R.id.ll_view_six,
                    R.id.ll_view_seven,
                    R.id.ll_view_eight,
                    R.id.ll_view_3_1,
                    R.id.ll_view_3_2,
                    R.id.ll_view_3_3,
                    R.id.ll_view_3_4
            );

        }

        @Override
        public void onBindView(int position) {

        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            switch (view.getId()){
                case R.id.ll_view_one:
                    ToastUtils.debugShow("功能正在书写");
//                startActivity(new Intent(getActivity(), ResidentActivity.class));
                    break;
                case R.id.ll_view_two:
                    getContext().startActivity(new Intent(getContext(), NewsListActivity.class));
                    break;
                case R.id.ll_view_three:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_four:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_five:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_six:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_seven:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_eight:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_3_1:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_3_2:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_3_3:
                    ToastUtils.debugShow("功能正在书写");
                    break;
                case R.id.ll_view_3_4:
                    ToastUtils.debugShow("功能正在书写");
                    break;

                default:

            }

        }
    }

    private final class ViewNormalHolder extends AppAdapter<?>.ViewHolder {

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
            GlideApp.with(getContext())
                    .load(bean.newsImageUrl)
                    .into(itemImg);
        }
    }
}