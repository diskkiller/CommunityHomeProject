package com.huaxixingfu.sqj.utils;

import android.graphics.Paint;
import android.widget.TextView;

public class ViewUtils {

    public static void setAntiAlias(TextView privateNtn){
        privateNtn.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        privateNtn.getPaint().setAntiAlias(true);//抗锯齿
    }

}
