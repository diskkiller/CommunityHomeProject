package com.huaxixingfu.sqj.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.SimpleMF;
import com.huaxixingfu.sqj.http.api.NotesListApi;

public class HomeSimpleMF<E extends NotesListApi.Bean> extends MarqueeFactory<TextView, E> {

    private HomeSimpleMFListener<E> clickListener;
    public HomeSimpleMF(Context mContext, HomeSimpleMFListener<E> clickListener) {
        super(mContext);
        this.clickListener = clickListener;
    }


    @Override
    public TextView generateMarqueeItemView(E data) {
        TextView mView = new TextView(mContext);
        mView.setText(data.appGuideTitle);
        mView.setOnClickListener(view->{
            if(clickListener != null){
                clickListener.onItemListener(data);
            }
        });
        return mView;
    }

    public interface HomeSimpleMFListener<E>{
        void onItemListener(E e);
    }
}
