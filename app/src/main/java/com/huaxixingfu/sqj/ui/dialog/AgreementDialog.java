package com.huaxixingfu.sqj.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.diskkiller.base.BaseDialog;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.commom.Constants;
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

        private CheckBox checkBox;

        public Builder(Context context) {
            super(context);
            setTitle(R.string.str_private_agreement_title);
            setCustomView(R.layout.dialog_yinsi);

            TextView tvYinsiDetail = findViewById(R.id.tv_yinsi_detail);
            TextView tvYonghu = findViewById(R.id.tv_yonghu_detail);

            checkBox = findViewById(R.id.check_xieyi);

            tvYinsiDetail.setOnClickListener(v -> BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate()));

            tvYonghu.setOnClickListener(v -> BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement()));
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
                if (!checkBox.isChecked()){
                    ToastUtils.show(R.string.str_select_agreement_tips);
                    return;
                }

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