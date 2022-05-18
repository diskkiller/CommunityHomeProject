package com.huaxixingfu.sqj.ui.dialog;

import android.Manifest;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.diskkiller.base.BaseDialog;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.other.LinkTouchMovementMethod;
import com.huaxixingfu.sqj.other.TouchableSpan;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.SPManager;

/**
 *    desc   : 隐私对话框
 */
public final class AgreementDialog {

    public static final class Builder
            extends CommonDialog.Builder<Builder> {

        @Nullable
        private OnListener mListener;

        public Builder(Context context) {
            super(context);
            setTitle(R.string.str_private_agreement_title);
            setCustomView(R.layout.dialog_yinsi);

            TextView tvContent = findViewById(R.id.tv_week_title);
            String content = getString(R.string.str_private_agreement_content_2);
            SpannableString mSpannableString = new SpannableString(content);
            TouchableSpan serviceSpan = new TouchableSpan(getColor( R.color.col_main),
                    getColor( R.color.col_main), getColor( R.color.color_ffffff)) {
                @Override
                public void onClick(View widget) {

                    BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
                }
            };
            TouchableSpan personSpan = new TouchableSpan(getColor( R.color.col_main),
                    getColor( R.color.col_main), getColor( R.color.color_ffffff)) {
                @Override
                public void onClick(View widget) {
                    BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());
                }
            };
            mSpannableString.setSpan(serviceSpan, 32, 38, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannableString.setSpan(personSpan, 39, 45, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tvContent.setMovementMethod(new LinkTouchMovementMethod());
            tvContent.setText(mSpannableString);
            }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {

                SPManager.instance(getContext()).put(Constants.IS_AGREEMENT,true);
                autoDismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirm();
                return;
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
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