package com.huaxixingfu.sqj.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.diskkiller.base.BaseDialog;
import com.huaxixingfu.sqj.R;
import com.zyyoona7.wheel.WheelView;

import java.util.ArrayList;

public final class PickerHightDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private OnListener mListener;

        private  TextView mTitleView,mCloseView,mConfirmView;

        private WheelView<String> wheelView;

        private String mData;
        private int mPosition;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.sqj_picker_hight_dialog);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setHeight(getResources().getDisplayMetrics().heightPixels / 3);

            mTitleView = findViewById(R.id.tv_date_title);
            mCloseView = findViewById(R.id.tv_date_closer);
            mConfirmView = findViewById(R.id.tv_date_confirm);
            wheelView = findViewById(R.id.wheelview);

            setOnClickListener(mCloseView,mConfirmView);

            wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelected(WheelView<String> wheelView, String data, int position) {
                    mData = data;
                    mPosition = position;
                }
            });


        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }
        public Builder setTitle(CharSequence text) {
            mTitleView.setText(text);
            return this;
        }

        public Builder setConfirm(@StringRes int id) {
            return setConfirm(getString(id));
        }
        public Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }
        public Builder setCancel(CharSequence text) {
            mCloseView.setText(text);
            return this;
        }

        public Builder setData(ArrayList list) {
            wheelView.setData(list);
            return this;
        }


        @Override
        public BaseDialog create() {
            return super.create();
        }

        @Override
        public void onClick(View view) {
            if (view == mConfirmView) {
                dismiss();
                if (mListener != null) {
                    mListener.onConfirm(mData,mPosition);
                }
            } else if (view == mCloseView) {
                dismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(String data,int position);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}