package com.huaxixingfu.sqj.ui.popup;

import android.content.Context;

import com.diskkiller.base.BasePopupWindow;
import com.huaxixingfu.sqj.R;

/**
 *    author : diskkiller
 *    time   : 2019/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class CopyPopup {

    public static final class Builder
            extends BasePopupWindow.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.copy_popup);
        }
    }
}