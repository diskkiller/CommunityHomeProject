package com.huaxixingfu.sqj.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.GroupListBean;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.WechatLayoutManager;
import com.othershe.combinebitmap.listener.OnSubItemClickListener;
import com.shehuan.niv.NiceImageView;

import java.util.List;

/**
 *    desc   : 通信录列表
 */
public final class GroupListAdapter extends BaseQuickAdapter<GroupListBean,BaseViewHolder> {

    List<GroupListBean> data;
    private LinearLayoutManager mLayoutManager;
    private boolean isApply;

    public GroupListAdapter(@Nullable List<GroupListBean> data,boolean isApply) {
        super(R.layout.sqj_item_group, data);
        this.data = data;
        this.isApply = isApply;

    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, GroupListBean contactsListBean) {
        baseViewHolder.setText(R.id.tv_nickname, contactsListBean.chatGroupNiceName);
        NiceImageView niv_avater = baseViewHolder.getView(R.id.niv_avater);
        if (isApply) {
            baseViewHolder.setGone(R.id.ll_apply,false);
        } else {
            baseViewHolder.setGone(R.id.ll_apply,true);
        }

        /*String [] urls = contactsListBean.userAvatarUrl.toArray(new String[]{});;

        CombineBitmap.init(getContext())
                .setLayoutManager(new WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setSize(45) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(1) // 单个图片之间的距离，单位dp，默认0dp
                //.setGapColor() // 单个图片间距的颜色，默认白色
                .setPlaceholder(R.drawable.image_error_ic) // 单个图片加载失败的默认显示图片
                .setUrls(urls) // 要加载的图片url数组
                //.setBitmaps() // 要加载的图片bitmap数组
                //.setResourceIds() // 要加载的图片资源id数组
                .setImageView(niv_avater) // 直接设置要显示图片的ImageView
                // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                *//*.setOnSubItemClickListener(new OnSubItemClickListener() {
                    @Override
                    public void onSubItemClick(int index) {

                    }
                })*//*
                // 加载进度的回调函数，若是不使用setImageView()方法，可在onComplete()完成最终图片的显示
                *//*.setProgressListener(new ProgressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {

                    }
                })*//*
                .build();*/

        GlideApp.with(getContext())
                .load(contactsListBean.avatar)
                .into(niv_avater);
    }

    public void setLayoutManager(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

}