package com.huaxixingfu.sqj.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.AboutUsApi;
import com.huaxixingfu.sqj.http.api.AgreementApi;
import com.huaxixingfu.sqj.http.api.PrivateApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.dialog.AgreementDialog;
import com.huaxixingfu.sqj.utils.SPManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        initAgreement();
        about();
        privateInfo();
        agreement();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 隐私协议
     */
    private void privateInfo() {

        EasyHttp.post(this)
                .api(new PrivateApi())
                .request(new HttpCallback<HttpData<AgreementApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<AgreementApi.Bean> data) {
                        if(data.getData() != null){
                            String detailUrl = data.getData().detailUrl;
                            SPManager.instance(getContext()).savePrivate(detailUrl);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });

    }

    /**
     * 用户协议
     */
    private void agreement() {

        EasyHttp.post(this)
                .api(new AgreementApi())
                .request(new HttpCallback<HttpData<AgreementApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<AgreementApi.Bean> data) {
                        if(data.getData() != null){
                            String detailUrl = data.getData().detailUrl;
                            SPManager.instance(getContext()).saveAgreement(detailUrl);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    /**
     * 关于我们
     */
    private void about() {
        EasyHttp.post(this)
                .api(new AboutUsApi())
                .request(new HttpCallback<HttpData<AgreementApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<AgreementApi.Bean> data) {
                        if(data.getData() != null){
                            String detailUrl = data.getData().detailUrl;
                            SPManager.instance(getContext()).saveAboutUs(detailUrl);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    /**
     * 准备隐私协议
     */
    private void initAgreement() {
        //是否同意过隐私协议
        boolean agreement = SPManager.instance(getContext()).getBoolean(Constants.IS_AGREEMENT,false);
        if (agreement) {
            initJump();
        } else {
            //隐私协议弹窗
            new AgreementDialog.Builder(this)
                    // 确定按钮文本
                    .setConfirm(getString(R.string.common_confirm))
                    // 设置 null 表示不显示取消按钮
                    .setCancel(getString(R.string.common_never))
                    // 设置点击按钮后不关闭对话框
//                    .setAutoDismiss(false)
                    .setCancelable(false)
                    .setListener(new AgreementDialog.OnListener() {

                        @Override
                        public void onConfirm() {
                            // TODO 由于应用市场审核的原因，加载时不再尝试获取权限
                            initJump();
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    /**
     * 准备跳转
     */
    private void initJump() {
        if(SPManager.instance(getContext()).isLogin())
            startActivity(new Intent(getContext(), HomeActivity.class));
        else
            startActivity(new Intent(getContext(), LoginActivity.class));
        finish();
    }

}
