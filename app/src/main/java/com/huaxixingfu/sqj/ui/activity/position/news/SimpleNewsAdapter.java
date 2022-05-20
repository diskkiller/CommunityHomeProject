package com.huaxixingfu.sqj.ui.activity.position.news;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

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
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.position.HallActivity;
import com.huaxixingfu.sqj.ui.fragment.HomeSimpleMF;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

/**
 *    desc   : 新闻列表 simple列表
 */
public final class SimpleNewsAdapter extends AppAdapter<HomeContentNewsApi.Bean.VContentNew> {

//    public static final int VIEW_TYPE_HEADER_BANNER = 1;
//    public static final int VIEW_TYPE_HEADER_MESSAGE_LIST = 3;
//    public static final int VIEW_TYPE_HEADER_LIST = 2;
//    public static final int VIEW_TYPE_THEN = 0;
//    public static final int VIEW_TYPE_NEW = 4;
//    private LifecycleOwner lifecycleOwner;
//    private OnHttpListener listener;
//    public SimpleMarqueeView marquee;

//    public SimpleNewsAdapter(Context context, LifecycleOwner lifecycleOwner, OnHttpListener listener) {
//        super(context);
//        this.lifecycleOwner =  lifecycleOwner;
//        this.listener =  listener;
//    }
    public SimpleNewsAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_HEADER_BANNER) {
//            return new HomeHeaderBanner(lifecycleOwner,listener);
//        } else if (viewType == VIEW_TYPE_HEADER_MESSAGE_LIST) {
//            HomeHeaderMessage homeHeaderMessage = new HomeHeaderMessage(lifecycleOwner, listener);
//            marquee = homeHeaderMessage.marquee;
//            return homeHeaderMessage;
//        } else if (viewType == VIEW_TYPE_HEADER_LIST) {
//            return new HomeHeaderList();
//        } else if (viewType == VIEW_TYPE_THEN) {
//            return new HomeHeaderNewTop();
//        } else {
            return new ViewNormalHolder();
//        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return VIEW_TYPE_HEADER_BANNER;
//        } else if (position == 1) {
//            return VIEW_TYPE_HEADER_MESSAGE_LIST;
//        } else if (position == 2) {
//            return VIEW_TYPE_HEADER_LIST;
//        } else if (position == 3) {
//            return VIEW_TYPE_THEN;
//        } else {
//            return VIEW_TYPE_NEW;
//        }
        return  super.getItemViewType(position);
    }

    private final class ViewNormalHolder extends ViewHolder {

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