package com.huaxixingfu.sqj.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.view.CountdownView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.GetCodeApi;
import com.huaxixingfu.sqj.http.api.LoginApi;
import com.huaxixingfu.sqj.http.api.RegisterApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.MatchUtils;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.Sm4Util;
import com.huaxixingfu.sqj.utils.ViewUtils;

import java.util.HashMap;

public class RegisterActivity extends AppActivity implements View.OnClickListener {

    private String account, code, pwd, confirmPwd;
    private CountdownView mTextCountTimer;
    private TextView mGetCode;
    private CheckBox checkContract;
    private EditText etAccount,etCode,etPwd,etConfirmPwd;
    private boolean isAgree = false;
    String phoneCode;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_register;
    }

    @Override
    protected void initView() {
        mTextCountTimer = findViewById(R.id.tv_get_code);
        setOnClickListener(R.id.tv_get_code,R.id.tv_tourist,R.id.tv_code_login,R.id.tv_submit,R.id.private_btn,R.id.about_btn);

        checkContract = findViewById(R.id.check_xieyi);
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);
        etPwd = findViewById(R.id.et_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);

        TextView privateNtn = findViewById(R.id.private_btn);
        TextView aboutNtn = findViewById(R.id.about_btn);
        ViewUtils.setAntiAlias(privateNtn);
        ViewUtils.setAntiAlias(aboutNtn);
    }

    @Override
    protected void initData() {}

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_submit) {
            //判断参数情况
            account = etAccount.getText().toString();
            code = etCode.getText().toString();
            pwd = etPwd.getText().toString();
            confirmPwd = etConfirmPwd.getText().toString();
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(R.string.input_code_hint);
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.show(R.string.pwd_error);
                return;
            }
            if (!MatchUtils.isRightPwd(pwd)) {
                ToastUtils.show(R.string.login_pwd_principle);
            }
            if (TextUtils.isEmpty(confirmPwd)) {
                ToastUtils.show(R.string.confirm_pwd_error);
                return;
            }
            if (!MatchUtils.isRightPwd(confirmPwd)) {
                ToastUtils.show(R.string.login_pwd_principle);
            }
            if (!pwd.equals(confirmPwd)) {
                ToastUtils.show("两次输入的密码不一致");
            }
            if (!checkContract.isChecked()) {
                ToastUtils.show(R.string.agree_user_contract);
                return;
            }

            String cipher = "";
            try {
                System.out.println("开始****************************");
                System.out.println("加密前："+pwd);
                cipher = Sm4Util.encryptEcb(Sm4Util.KEY,pwd);//sm4加密
                System.out.println("加密后："+cipher);
                System.out.println("校验："+Sm4Util.verifyEcb(Sm4Util.KEY,cipher,pwd));//校验加密前后是否为同一数据
            } catch (Exception e) {
                e.printStackTrace();
            }

            register(account, code, cipher.toUpperCase(), cipher.toUpperCase());
        } else if (id == R.id.tv_code_login) {
            finish();
        } else if (id == R.id.tv_get_code) {
            account = etAccount.getText().toString();
            //判断参数情况
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            getCode(account);
        } else if (id == R.id.tv_tourist) {
            // 进行内存优化，销毁所有界面
            ActivityManager.getInstance().finishAllActivities();
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }else if (id == R.id.private_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());
        } else if (id == R.id.about_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
        }

    }

    private void getCode(String account){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        EasyHttp.post(this)
                .api(new GetCodeApi())
                .json(map)
                .request(new HttpCallback<HttpData<GetCodeApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<GetCodeApi.Bean> data) {
                        if(data.getData() != null){
                            GetCodeApi.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            ToastUtils.show("发送验证码成功");
                            mTextCountTimer.start();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void register(String account, String code, String password, String confirmPassword){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("account", account);
        hashMap.put("code", code);
        hashMap.put("password", password);
        hashMap.put("confirmPassword", confirmPassword);
        EasyHttp.post(this)
                .api(new RegisterApi())
                .json(hashMap)
                .request(new HttpCallback<HttpData<RegisterApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<RegisterApi.Bean> data) {
                        if(data.getData() != null){
                            RegisterApi.Bean model = data.getData();
                            if(model.status){
                                finish();
                                ToastUtils.show("注册成功");
                            }else{
                                ToastUtils.show(model.message);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
}