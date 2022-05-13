package com.huaxixingfu.sqj.ui.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
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

public class NewsListActivity extends AppActivity {

    private MagicIndicator magic;
    private ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_news_list;
    }

    @Override
    protected void initView() {
        magic = findViewById(R.id.magic);
        viewPager = findViewById(R.id.viewPager);
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
                                setColumnData(columns);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    /**
     * 设置新闻分类
     * @param columns
     */
    private void setColumnData( List<NewsColumnApi.Bean>  columns){

        List<Fragment> fragments = new ArrayList<>(columns.size());
        for (int i=0,j=columns.size();i<j;i++){
            NewsColumnApi.Bean column = columns.get(i);
            NewsColumnFragment fragment = NewsColumnFragment.newInstance(column.newsColumnCode);
            fragments.add(fragment);
        }
        viewPager.setAdapter(new NewsColumnAdapter(this.getSupportFragmentManager(),fragments));

        MagicIndicator magicIndicator = magic;
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return columns == null ? 0 : columns.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#FF333333"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#FFEC3937"));
                colorTransitionPagerTitleView.setText(columns.get(index).newsColumnName);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                //  public static final int MODE_MATCH_EDGE = 0;   // 直线宽度 == title宽度 - 2 * mXOffset
                //   public static final int MODE_WRAP_CONTENT = 1;    // 直线宽度 == title内容宽度 - 2 * mXOffset
                //   public static final int MODE_EXACTLY = 2;  // 直线宽度 == mLineWidth
                //TODO 线的宽度
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(50);
                indicator.setColors(getResources().getColor(R.color.color_ffec3937));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

    }

    public class NewsColumnAdapter extends FragmentPagerAdapter{
        List<Fragment> fragments;

        public NewsColumnAdapter(@NonNull @NotNull FragmentManager fm,
                                 List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @NotNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
