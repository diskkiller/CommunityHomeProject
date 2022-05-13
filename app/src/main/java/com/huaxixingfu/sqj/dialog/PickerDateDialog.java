package com.huaxixingfu.sqj.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.diskkiller.base.BaseDialog;
import com.huaxixingfu.sqj.R;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.ex.DayWheelView;
import com.zyyoona7.picker.ex.MonthWheelView;
import com.zyyoona7.picker.ex.YearWheelView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.Date;

public final class PickerDateDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private OnListener mListener;

        private  TextView mTitleView,mCloseView,mConfirmView;

        private String mDate;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.sqj_picker_date_dialog);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setHeight(getResources().getDisplayMetrics().heightPixels / 3);

            mTitleView = findViewById(R.id.tv_date_title);
            mCloseView = findViewById(R.id.tv_date_closer);
            mConfirmView = findViewById(R.id.tv_date_confirm);
            setOnClickListener(mConfirmView,mCloseView);

            final DatePickerView customDpv3 = findViewById(R.id.dpv_custom_3);
            customDpv3.setTextSize(24, true);
            customDpv3.setShowLabel(false);
            customDpv3.setTextBoundaryMargin(16, true);
            customDpv3.setShowDivider(true);
            customDpv3.setDividerType(WheelView.DIVIDER_TYPE_FILL);
            customDpv3.setDividerColor(Color.parseColor("#9e9e9e"));
            customDpv3.setDividerPaddingForWrap(10, true);
            YearWheelView yearWv3 = customDpv3.getYearWv();
            MonthWheelView monthWv3 = customDpv3.getMonthWv();
            DayWheelView dayWv3 = customDpv3.getDayWv();
            yearWv3.setIntegerNeedFormat("%d年");
            monthWv3.setIntegerNeedFormat("%d月");
            dayWv3.setIntegerNeedFormat("%02d日");
            yearWv3.setCurvedArcDirection(WheelView.CURVED_ARC_DIRECTION_LEFT);
            yearWv3.setCurvedArcDirectionFactor(0.65f);
            dayWv3.setCurvedArcDirection(WheelView.CURVED_ARC_DIRECTION_RIGHT);
            dayWv3.setCurvedArcDirectionFactor(0.65f);

            customDpv3.setOnDateSelectedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(BaseDatePickerView datePickerView, int year,
                                           int month, int day, @Nullable Date date) {
                    //                Toast.makeText(Main3Activity.this,"选中的日期："+date.toString(),Toast.LENGTH_SHORT).show();
                    mDate = year + "-" + month + "-" + day;
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


        @Override
        public BaseDialog create() {
            return super.create();
        }

        @Override
        public void onClick(View view) {
            if (view == mConfirmView) {
                dismiss();
                if (mListener != null) {
                    mListener.onConfirm(mDate);
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
        void onConfirm(String date);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}