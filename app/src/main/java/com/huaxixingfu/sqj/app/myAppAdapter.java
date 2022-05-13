package com.huaxixingfu.sqj.app;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;


/**
 *    desc   : RecyclerView 适配器业务基类
 */
public abstract class myAppAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


    public myAppAdapter(int layoutResId) {
        super(layoutResId);
    }
}