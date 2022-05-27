package com.huaxixingfu.sqj.ui.activity.me;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.OnHttpListener;
import com.diskkiller.widget.layout.SettingBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.AppVersionApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.CacheDataManager;
import com.huaxixingfu.sqj.manager.ThreadPoolManager;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.dialog.UpdateDialog;
import com.huaxixingfu.sqj.utils.SPManager;

import java.io.File;
import java.util.HashMap;

public class SettingActivity extends AppActivity {

    private SettingBar sb_setting_about,sb_setting_privacy_policy,
            sb_setting_agreement,sb_setting_cache,sb_setting_update;
    private ImageView iv_update_tip;
    private AppVersionApi.Bean data;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_setting_activity;
    }

    public void initView() {
        sb_setting_about = findViewById(R.id.sb_setting_about);
        sb_setting_privacy_policy = findViewById(R.id.sb_setting_privacy_policy);
        sb_setting_agreement = findViewById(R.id.sb_setting_agreement);
        sb_setting_cache = findViewById(R.id.sb_setting_cache);
        sb_setting_update = findViewById(R.id.sb_setting_update);
        iv_update_tip = findViewById(R.id.iv_update_tip);

        setOnClickListener(sb_setting_about,sb_setting_privacy_policy,
                sb_setting_update,sb_setting_agreement,sb_setting_cache);

    }


    public void initData() {
        // 获取应用缓存大小
        sb_setting_cache.setRightText(CacheDataManager.getTotalCacheSize(this));
        checkUpdate();
    }

    private void checkUpdate(){
        HashMap<String, Object> map= new HashMap<>();
        map.put("appVersionNum", BuildConfig.VERSION_CODE);
        EasyHttp.post(this)
                .api(new AppVersionApi())
                .json(map)
                .request(new OnHttpListener<HttpData<AppVersionApi.Bean>>() {
                    @Override
                    public void onSucceed(HttpData<AppVersionApi.Bean> result) {
                        if (result.getData() != null){
                            iv_update_tip.setVisibility(View.VISIBLE);
                            data = result.getData();
                        }else {
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_PATH);
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_VERSION);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtils.show(e.getMessage());
                    }
                });
    }

    private void showUpdateDialog(){
        if (data == null){
            ToastUtils.show("已经是最新版本了");
            return;
        }
        String savePath = SPManager.instance(this).getString(Constants.UPDATE_PATH);
        String saveVersion = SPManager.instance(this).getString(Constants.UPDATE_VERSION);
        File file = null;
        if (!TextUtils.isEmpty(saveVersion) && saveVersion.equals(data.appVersionNum)){
            if (!TextUtils.isEmpty(savePath)){
                file = new File(savePath);
            }
        }

        new UpdateDialog.Builder(this)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setUpdateLog(data.appVersionContent)
                .setVersionName(data.appVersionNum)
                .setUpdateTitle(data.appVersionTitle)
                .setForceUpdate(data.appVersionUpdateFlag != 0)
                .setOnCompleteListener(new UpdateDialog.OnCompleteListener() {
                    @Override
                    public void onComplete(File file, String url) {
                        SPManager.instance(getContext()).put(Constants.UPDATE_VERSION, data.appVersionNum);
                        SPManager.instance(getContext()).put(Constants.UPDATE_PATH, file.getAbsolutePath());
                    }
                })
                .setExistsApkFile(file)
                .show();
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

        }else if (id == R.id.sb_setting_update){
            showUpdateDialog();
        }
    }
}
