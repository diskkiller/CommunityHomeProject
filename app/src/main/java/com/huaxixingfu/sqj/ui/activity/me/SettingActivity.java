package com.huaxixingfu.sqj.ui.activity.me;

import android.view.View;
import com.diskkiller.widget.layout.SettingBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.manager.CacheDataManager;
import com.huaxixingfu.sqj.manager.ThreadPoolManager;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.SPManager;

public class SettingActivity extends AppActivity {

    private SettingBar sb_setting_about,sb_setting_privacy_policy,sb_setting_agreement,sb_setting_cache;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_setting_activity;
    }

    public void initView() {
        sb_setting_about = findViewById(R.id.sb_setting_about);
        sb_setting_privacy_policy = findViewById(R.id.sb_setting_privacy_policy);
        sb_setting_agreement = findViewById(R.id.sb_setting_agreement);
        sb_setting_cache = findViewById(R.id.sb_setting_cache);

        setOnClickListener(sb_setting_about,sb_setting_privacy_policy,sb_setting_agreement,sb_setting_cache);

    }


    public void initData() {
        // 获取应用缓存大小
        sb_setting_cache.setRightText(CacheDataManager.getTotalCacheSize(this));
    }



    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.sb_setting_about) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAboutUs());
        }else if(id == R.id.sb_setting_agreement){
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
        }else if(id == R.id.sb_setting_privacy_policy){
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());

        }else if(id == R.id.sb_setting_cache){

            // 清除内存缓存（必须在主线程）
            GlideApp.get(getActivity()).clearMemory();
            ThreadPoolManager.getInstance().execute(() -> {
                CacheDataManager.clearAllCache(this);
                // 清除本地缓存（必须在子线程）
                GlideApp.get(getActivity()).clearDiskCache();
                post(() -> {
                    // 重新获取应用缓存大小
                    sb_setting_cache.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                });
            });

        }
    }
}
