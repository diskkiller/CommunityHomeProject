package com.huaxixingfu.sqj.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.VNotes;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;

import org.jetbrains.annotations.NotNull;

/**
 * 作者：lblbh on 2022/4/30 16:54
 * 邮箱：lanbuhan@163.com
 * 功能：系统通知-适配器
 */

public class SystemNotesAdapter extends BaseQuickAdapter<VNotes, BaseViewHolder> {
    public SystemNotesAdapter(int layoutResId) {
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
    protected void convert(@NotNull BaseViewHolder baseViewHolder, VNotes model) {
        TextView itemHint = baseViewHolder.getView(R.id.item_hint);
        TextView itemContent = baseViewHolder.getView(R.id.item_content);
        TextView itemFind = baseViewHolder.getView(R.id.item_find);
        TextView itemTime = baseViewHolder.getView(R.id.item_time);

        itemHint.setText(model.appGuideTitle);
        itemContent.setText(model.appGuideContent);
        itemTime.setText(model.modifiedAt);

        itemFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BrowserActivity.start(getContext(),model.detailUrl);
            }
        });

    }
}
