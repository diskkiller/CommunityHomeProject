package com.huaxixingfu.sqj.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseUpFetchModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.Message;
import com.huaxixingfu.sqj.utils.TimeUtils;
import com.shehuan.niv.NiceImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @Description: java类作用描述
 */
public class MsgAdapter extends BaseMultiItemQuickAdapter<Message, BaseViewHolder> implements UpFetchModule {


    public MsgAdapter(List<Message> data) {
        super(data);
        addItemType(Message.TEXT_ME, R.layout.sqj_item_msg_text_me);
        addItemType(Message.TEXT_YOU, R.layout.sqj_item_msg_text_you);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder vh, Message message) {


        switch (getItemViewType(getItemPosition(message))) {
            case Message.TEXT_ME:
            case Message.TEXT_YOU:
                vh.setText(R.id.tv_content, message.msg.content)
                        .setText(R.id.tv_time, TimeUtils.formatTime(message.msg.systemTime, TimeUtils.TIME_FORMAT_STYLE_YS));
                TextView tv_time = vh.getView(R.id.tv_time);
                tv_time.setVisibility(message.timeFlag ? View.VISIBLE : View.GONE);
                Glide.with(getContext()).load("").placeholder(R.mipmap.icon_logo)
                        .error(R.mipmap.icon_logo).into((NiceImageView) vh.getView(R.id.niv_avater));
                break;
        }
    }

    @NotNull
    @Override
    public BaseUpFetchModule addUpFetchModule(@NotNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseUpFetchModule(baseQuickAdapter);
    }
}
