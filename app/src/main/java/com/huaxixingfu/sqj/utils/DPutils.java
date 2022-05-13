package com.huaxixingfu.sqj.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class DPutils implements View.OnClickListener {

    //dp转换成像素
    public static int dp2px(int value, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    //像素转换成dp
    public static int dp2sp(int value, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取view 宽高
     *
     * @param layout
     * @return
     */
    public static int[] getViewHeightAndtWidth(final View layout) {
        final int[] hw = new int[2];
        layout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        layout.getViewTreeObserver().removeOnPreDrawListener(this);
                        hw[0] = layout.getMeasuredHeight();
                        hw[1] = layout.getMeasuredWidth();
                        return false;
                    }
                });
        return hw;
    }

    //获取屏幕高度
    public static int getWinHight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕高度（像素）
        return dm.heightPixels;
    }


    @Override
    public void onClick(View v) {

    }
}
