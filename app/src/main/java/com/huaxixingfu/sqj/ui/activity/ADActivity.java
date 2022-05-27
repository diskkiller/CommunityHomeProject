package com.huaxixingfu.sqj.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.http.listener.OnHttpListener;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.ADBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.ADApi;
import com.huaxixingfu.sqj.http.api.AppVersionApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.dialog.UpdateDialog;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.io.File;
import java.util.HashMap;


public class ADActivity extends AppActivity implements View.OnClickListener {

    private CountDownTimer mDownTimer;
    private boolean mIsStopTimer;
    private TextView mVersionTV;
    private ADBean mAdBean;
    private CountDownTimer mFirstDownTimer;
    private boolean isDown = false;//是否下载图片
    private String imgUrl,title,contentUrl;

    private ImageView imageView;
    private TextView tv_close;
    private  String adPicUrl,adDetailUrl;
    private int timeSecond = 3;
    private int timeUnit = 1000;


    @Override
    protected int getLayoutId() {
        return R.layout.sqj_ad_layout;
    }

    @Override
    protected void initView() {
        imageView = findViewById(R.id.splash_iv);
        tv_close = findViewById(R.id.tv_close);
        setOnClickListener(R.id.splash_iv,R.id.tv_close);

        tv_close.setText(String.format(getString(R.string.AD_time_close),timeSecond));
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    protected void initData() {
        adDetail();
    }

    private void adDetail() {
        EasyHttp.post(this)
                .api(new ADApi())
                .request(new HttpCallback<HttpData<ADBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<ADBean> data) {
                        checkUpdate();
                        if(data.getData() != null){
                            adPicUrl = data.getData().appGuideImageUrl;
                            adDetailUrl = data.getData().appGuideUrl;
                            if(StringUtils.isEmpty(adPicUrl)){
                                jumpAct();
                            }
                            GlideApp.with(getContext())
                                    .load(adPicUrl)
                                    .placeholder(R.color.color_ffffff)
                                    .centerCrop()
                                    .into(imageView);

                            //显示广告图片，开始倒计时
                            mDownTimer = new CountDownTimer(timeSecond * timeUnit, timeUnit) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                    tv_close.setText(String.format(getString(R.string.AD_time_close),
                                            millisUntilFinished/timeUnit));
                                }

                                @Override
                                public void onFinish() {
                                    if (!mIsStopTimer) {
                                        jumpAct();
                                    }
                                }
                            };
                            mDownTimer.start();
                            //注意，如果点击进入广告内容activity，中断mDownTimer的计时，防止首页打开
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        jumpAct();
                    }
                });
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
                            if (mDownTimer != null){
                                mDownTimer.cancel();
                            }
                            showUpdateDialog(result.getData());
                        }else {
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_PATH);
                            SPManager.instance(getContext()).deleteKey(Constants.UPDATE_VERSION);
                            if (mDownTimer == null){
                                jumpAct();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        if (mDownTimer == null){
                            jumpAct();
                        }
                    }
                });
    }

    private void showUpdateDialog(AppVersionApi.Bean data){
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
                .addOnDismissListener(new BaseDialog.OnDismissListener() {
                    @Override
                    public void onDismiss(BaseDialog dialog) {
                        jumpAct();
                    }
                })
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


    private void jumpAct(){
        if(SPManager.instance(getContext()).isLogin())
            startActivity(new Intent(getContext(), HomeActivity.class));
        else
            startActivity(new Intent(getContext(), LoginActivity.class));
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_close:

                if(SPManager.instance(getContext()).isLogin())
                    startActivity(new Intent(getContext(), HomeActivity.class));
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                finish();
                break;
            case R.id.splash_iv:
                //toast(博客介绍使用,实际开发中应该删除)
                /*showToast("点击广告图片,进入详情");
                stopDownTimer();
                startActivity(new Intent(SplashActivity.this, CommonActivity.class).putExtra(TYPE_KEY, 4));
*/
                /*Intent intent = new Intent(this, ADActivity.class);
                intent.putExtra(ConstantUtil.Intent.AD_TITLE, mAdBean.getTitle());
                intent.putExtra(ConstantUtil.Intent.AD_CONTENT_URL, mAdBean.getContentUrl());
                startActivity(intent);*/
                if(StringUtils.isEmpty(adDetailUrl))return;
                BrowserActivity.start(getActivity(), adDetailUrl);
                stopDownTimer();
                finish();
                break;
        }
    }

    private void stopDownTimer() {
        if (mDownTimer != null) {
            mDownTimer.cancel();
        }
        mIsStopTimer = true;
    }


}
