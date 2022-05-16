package com.huaxixingfu.sqj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 *
 * @ClassName: ExpandGridView
 * @Description: 可扩展的gridview
 */
public class ExpandGridView extends GridView {

    public ExpandGridView(Context context) {
        super(context);
    }

    public ExpandGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
