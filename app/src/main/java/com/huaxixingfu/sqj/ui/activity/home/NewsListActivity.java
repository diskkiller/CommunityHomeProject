package com.huaxixingfu.sqj.ui.activity.home;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：社会新闻列表
 */

public class NewsListActivity extends AppActivity implements  ViewPager.OnPageChangeListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_news_list;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.magic);
        mViewPager = findViewById(R.id.viewPager);
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    protected void initData(){
        EasyHttp.post(this)
                .api(new NewsColumnApi())
                .request(new HttpCallback<HttpData<List<NewsColumnApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<NewsColumnApi.Bean>> data) {
                        if(data.getData() != null){
                            List<NewsColumnApi.Bean>  columns = data.getData();
                            if((null != columns) && (columns.size()>0)){

                                for (NewsColumnApi.Bean column : columns) {
                                    mPagerAdapter.addFragment(NewsColumnFragment.newInstance(column.newsColumnCode), column.newsColumnName);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
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
