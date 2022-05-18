package com.huaxixingfu.sqj.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.ADBean;
import com.huaxixingfu.sqj.http.api.ADApi;
import com.huaxixingfu.sqj.http.api.CancleAccountApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.shehuan.niv.NiceImageView;


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
                        if(data.getData() != null){
                            adPicUrl = data.getData().appGuideImageUrl;
                            adDetailUrl = data.getData().appGuideUrl;
                            GlideApp.with(getContext())
                                    .load(adPicUrl)
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
                                        if(SPManager.instance(getContext()).isLogin())
                                            startActivity(new Intent(getContext(), HomeActivity.class));
                                        else
                                            startActivity(new Intent(getContext(), LoginActivity.class));
                                        finish();
                                    }
                                }
                            };
                            mDownTimer.start();
                            //注意，如果点击进入广告内容activity，中断mDownTimer的计时，防止首页打开
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        if(SPManager.instance(getContext()).isLogin())
                            startActivity(new Intent(getContext(), HomeActivity.class));
                        else
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        finish();
                    }
                });
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
