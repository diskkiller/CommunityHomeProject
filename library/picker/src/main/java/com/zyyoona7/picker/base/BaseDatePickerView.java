package com.zyyoona7.picker.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zyyoona7.picker.ex.DayWheelView;
import com.zyyoona7.picker.ex.MonthWheelView;
import com.zyyoona7.picker.ex.YearWheelView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.picker.listener.OnPickerScrollStateChangedListener;
import com.zyyoona7.wheel.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class BaseDatePickerView extends FrameLayout implements WheelView.OnItemSelectedListener<Integer>,
        WheelView.OnWheelChangedListener {

    private final SimpleDateFormat mYmdSdf;
    private final SimpleDateFormat mYmSdf;
    protected YearWheelView mYearWv;
    protected MonthWheelView mMonthWv;
    protected DayWheelView mDayWv;

    private OnDateSelectedListener mOnDateSelectedListener;
    private OnPickerScrollStateChangedListener mOnPickerScrollStateChangedListener;

    public BaseDatePickerView(@NonNull Context context) {
        this(context, null);
    }

    public BaseDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseDatePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mYmdSdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        mYmSdf = new SimpleDateFormat("yyyy-M", Locale.getDefault());
        if (getDatePickerViewLayoutId() != 0) {
            LayoutInflater.from(context).inflate(getDatePickerViewLayoutId(), this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int yearId = getYearWheelViewId();
        if (hasViewId(yearId)) {
            mYearWv = findViewById(yearId);
        }
        int monthId = getMonthWheelViewId();
        if (hasViewId(monthId)) {
            mMonthWv = findViewById(monthId);
        }
        int dayId = getDayWheelViewId();
        if (hasViewId(dayId)) {
            mDayWv = findViewById(dayId);
        }
        if (mYearWv != null) {
            mYearWv.setOnItemSelectedListener(this);
            mYearWv.setOnWheelChangedListener(this);
        }
        if (mMonthWv != null) {
            mMonthWv.setOnItemSelectedListener(this);
            mMonthWv.setOnWheelChangedListener(this);
            if (mYearWv != null) {
                mMonthWv.setCurrentSelectedYear(mYearWv.getSelectedYear());
            }
        }
        if (mDayWv != null) {
            mDayWv.setOnItemSelectedListener(this);
            mDayWv.setOnWheelChangedListener(this);
            if (mYearWv != null && mMonthWv != null) {
                mDayWv.setYearAndMonth(mYearWv.getSelectedYear(), mMonthWv.getSelectedMonth());
            }
        }
    }

    /**
     * ?????? datePickerView ???????????????id
     *
     * @return ????????????id
     */
    @LayoutRes
    protected abstract int getDatePickerViewLayoutId();

    /**
     * ?????? YearWheelView ??? id
     *
     * @return YearWheelView id
     */
    @IdRes
    protected abstract int getYearWheelViewId();

    /**
     * ?????? MonthWheelView ??? id
     *
     * @return MonthWheelView id
     */
    @IdRes
    protected abstract int getMonthWheelViewId();

    /**
     * ?????? DayWheelView id
     *
     * @return DayWheelView id
     */
    @IdRes
    protected abstract int getDayWheelViewId();

    @Override
    public void onItemSelected(WheelView<Integer> wheelView, Integer data, int position) {
        if (wheelView.getId() == getYearWheelViewId()) {
            //????????????
            if (mDayWv != null) {
                mDayWv.setYear(data);
            }
            if (mMonthWv != null) {
                mMonthWv.setCurrentSelectedYear(data);
            }
        } else if (wheelView.getId() == getMonthWheelViewId()) {
            //????????????
            if (mDayWv != null) {
                mDayWv.setMonth(data);
            }
        }

        int year = mYearWv == null ? 1970 : mYearWv.getSelectedYear();
        int month = mMonthWv == null ? 1 : mMonthWv.getSelectedMonth();
        int day = mDayWv == null ? 1 : mDayWv.getSelectedDay();
        String date = year + "-" + month + "-" + day;
        if (mOnDateSelectedListener != null) {
            try {
                Date formatDate = null;
                if (isAllShown()) {
                    //??????????????????????????????????????????????????????
                    formatDate = mYmdSdf.parse(date);
                } else if (isYmShown()) {
                    //?????????????????????????????????????????????????????????
                    formatDate = mYmSdf.parse(date);
                }
                mOnDateSelectedListener.onDateSelected(this, year,
                        month, day, formatDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWheelScroll(int scrollOffsetY) {

    }

    @Override
    public void onWheelItemChanged(int oldPosition, int newPosition) {

    }

    @Override
    public void onWheelSelected(int position) {

    }

    @Override
    public void onWheelScrollStateChanged(int state) {
        if (mOnPickerScrollStateChangedListener != null) {
            mOnPickerScrollStateChangedListener.onScrollStateChanged(state);
        }
    }

    private boolean isAllShown() {
        return isYmShown()
                && mDayWv != null && mDayWv.getVisibility() == VISIBLE;
    }

    private boolean isYmShown() {
        return mYearWv != null && mYearWv.getVisibility() == VISIBLE
                && mMonthWv != null && mMonthWv.getVisibility() == VISIBLE;
    }

    private boolean hasViewId(@IdRes int idRes) {
        return idRes != 0 && idRes != NO_ID;
    }

    /**
     * ???????????? WheelView
     *
     * @return ?????? WheelView
     */
    public YearWheelView getYearWv() {
        return mYearWv;
    }

    /**
     * ???????????? WheelView
     *
     * @return ?????? WheelView
     */
    public MonthWheelView getMonthWv() {
        return mMonthWv;
    }

    /**
     * ????????? WheelView
     *
     * @return ??? WheelView
     */
    public DayWheelView getDayWv() {
        return mDayWv;
    }


    /**
     * ???????????????????????????
     *
     * @param onDateSelectedListener ?????????????????????
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    /**
     * ??????????????????????????????
     *
     * @param listener ???????????????????????????
     */
    public void setOnPickerScrollStateChangedListener(OnPickerScrollStateChangedListener listener) {
        mOnPickerScrollStateChangedListener = listener;
    }

    /**
     * ??????????????????????????? ?????????????????????????????????????????????
     *
     * @param calendar Calendar
     */
    public void setMaxDate(@NonNull Calendar calendar) {
        setMaxDate(calendar.getTime());
    }

    /**
     * ??????????????????????????? ?????????????????????????????????????????????
     *
     * @param date ??????
     */
    public void setMaxDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int maxYear = calendar.get(Calendar.YEAR);
        int maxMonth = calendar.get(Calendar.MONTH) + 1;
        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (mYearWv != null) {
            mYearWv.setMaxYear(maxYear);
        }
        if (mMonthWv != null) {
            mMonthWv.setMaxYearAndMonth(maxYear, maxMonth);
        }
        if (mDayWv != null) {
            mDayWv.setMaxYearMonthAndDay(maxYear, maxMonth, maxDay);
        }
    }

    /**
     * ??????????????????????????? ?????????????????????????????????????????????
     *
     * @param calendar Calendar
     */
    public void setMinDate(@NonNull Calendar calendar) {
        setMinDate(calendar.getTime());
    }

    /**
     * ??????????????????????????? ?????????????????????????????????????????????
     *
     * @param date ??????
     */
    public void setMinDate(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minYear = calendar.get(Calendar.YEAR);
        int minMonth = calendar.get(Calendar.MONTH) + 1;
        int minDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (mYearWv != null) {
            mYearWv.setMinYear(minYear);
        }
        if (mMonthWv != null) {
            mMonthWv.setMinYearAndMonth(minYear, minMonth);
        }
        if (mDayWv != null) {
            mDayWv.setMinYearMonthAndDay(minYear, minMonth, minDay);
        }
    }
}
