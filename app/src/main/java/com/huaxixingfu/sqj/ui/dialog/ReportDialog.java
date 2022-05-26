package com.huaxixingfu.sqj.ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.diskkiller.base.BaseDialog;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.utils.SPManager;

/**
 *    desc   : 举报对话
 */
public final class ReportDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        @Nullable
        private OnListener mListener;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_report);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);

            findViewById(R.id.tv_report).setOnClickListener(this);
            findViewById(R.id.tv_cancel).setOnClickListener(this);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_report) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirm();
                return;
            } else if (viewId == R.id.tv_cancel) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onCancel(getDialog());
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm();

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}