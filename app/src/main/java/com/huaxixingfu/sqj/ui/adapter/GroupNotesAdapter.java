package com.huaxixingfu.sqj.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.VGroupNotes;
import com.huaxixingfu.sqj.bean.VNotes;

import org.jetbrains.annotations.NotNull;

/**
 * 作者：lblbh on 2022/4/30 16:54
 * 邮箱：lanbuhan@163.com
 * 功能：群公告-适配器
 */

public class GroupNotesAdapter extends BaseQuickAdapter<VGroupNotes, BaseViewHolder> {
    public GroupNotesAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 清空当前数据
     */
    public void clearData() {
        if (getData() == null || getData().size() == 0) {
            return;
        }

        getData().clear();
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VGroupNotes model) {
        TextView itemContent = baseViewHolder.getView(R.id.item_content);
        TextView itemTime = baseViewHolder.getView(R.id.item_time);


        itemContent.setText(model.chatGroupNoticeContent);
        itemTime.setText(model.createdAt);

    }
}
