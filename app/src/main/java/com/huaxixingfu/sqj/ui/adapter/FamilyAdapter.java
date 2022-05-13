package com.huaxixingfu.sqj.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.http.api.FamilyDataApi;

import org.jetbrains.annotations.NotNull;

/**
 * 作者：lblbh on 2022/4/30 16:54
 * 邮箱：lanbuhan@163.com
 * 功能：家庭成员-适配器
 */

public class FamilyAdapter extends BaseQuickAdapter<FamilyDataApi.Bean.VFamily, BaseViewHolder> {
    public FamilyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, FamilyDataApi.Bean.VFamily model) {
        ImageView icIcon = baseViewHolder.getView(R.id.ic_icon);
        TextView tvName = baseViewHolder.getView(R.id.tv_name);
        TextView tvTitle = baseViewHolder.getView(R.id.tv_title);
        TextView tvFam = baseViewHolder.getView(R.id.tv_fam);

        Glide.with(icIcon).load(model.userAvatarUrl).into(icIcon);
        tvName.setText(model.userName);
        tvTitle.setText("手机号码:"+model.phoneNum);
        tvFam.setVisibility((model.flag)? View.VISIBLE:View.GONE);


    }
}
