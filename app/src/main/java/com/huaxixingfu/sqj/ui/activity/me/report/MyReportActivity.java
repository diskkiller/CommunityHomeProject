package com.huaxixingfu.sqj.ui.activity.me.report;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.diskkiller.base.FragmentPagerAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.google.android.material.tabs.TabLayout;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.http.api.NewsColumnApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.fragment.NewsColumnFragment;
import com.huaxixingfu.sqj.ui.fragment.report.ReportMeFragment;
import com.huaxixingfu.sqj.ui.fragment.report.ReportRecordFragment;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 功能：我的举报
 */

public class MyReportActivity extends AppActivity implements  ViewPager.OnPageChangeListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter<AppFragment<MyReportActivity>> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_report_my;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.magic);
        mViewPager = findViewById(R.id.viewPager);
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.post(()->{
            mPagerAdapter.addFragment(ReportMeFragment.newInstance(), getString(R.string.report_act_my_tab_0));
            mPagerAdapter.addFragment(ReportRecordFragment.newInstance(""), getString(R.string.report_act_my_tab_1));
//            setIndicator(mTabLayout, 20, 40);

        });

    }

    /**
     * @desc:  设置tab指示器的宽度
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    protected void initData(){

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
