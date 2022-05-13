package com.huaxixingfu.sqj.ui.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.diskkiller.http.EasyConfig;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.http.api.LoginApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.utils.SPManager;

import java.util.HashMap;

public class LoginActivity extends AppActivity{

    private String account, pwd;
    private CheckBox checkContract;
    private EditText etAccount,etPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_login;
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_submit).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_code_login).setOnClickListener(this);
        findViewById(R.id.tv_forget_pwd).setOnClickListener(this);
        findViewById(R.id.tv_tourist).setOnClickListener(this);
        findViewById(R.id.private_btn).setOnClickListener(this);
        findViewById(R.id.about_btn).setOnClickListener(this);
        checkContract = findViewById(R.id.check_xieyi);

        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);

        setOnClickListener(R.id.tv_submit,R.id.tv_register,R.id.tv_code_login,
                R.id.tv_forget_pwd,R.id.tv_tourist,R.id.private_btn,R.id.about_btn);

    }

    public void initData() {

    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_submit) {
            //调用登录
            //判断参数情况
            account = etAccount.getText().toString();
            pwd = etPwd.getText().toString();
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.show(R.string.pwd_error);
                return;
            }
            if (!checkContract.isChecked()) {
                ToastUtils.show(R.string.agree_user_contract);
                return;
            }
            login(account, pwd);
        } else if (id == R.id.tv_register) {
            //调用注册
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        } else if (id == R.id.tv_code_login) {
            //验证码登录
            startActivity(new Intent(getActivity(), LoginCodeActivity.class));
            finish();
        } else if (id == R.id.tv_forget_pwd) {
            //修改密码
            startActivity(new Intent(getActivity(), ForgetPwdActivity.class));
        } else if (id == R.id.tv_tourist) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            finish();
        } else if (id == R.id.private_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());
        } else if (id == R.id.about_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
        }

    }

    private void login(String account, String pwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("password", pwd);
        EasyHttp.post(this)
                .api(new LoginApi())
                .json(map)
                .request(new HttpCallback<HttpData<LoginApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<LoginApi.Bean> data) {
                        if(data.getData() != null){
                            LoginApi.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            //更新token
                            SPManager.instance(getContext()).setToken(model.token);
                            //更新用户
                            SPManager.instance(getContext()).putModel(Constants.USERDATA, model);

                            SPManager.instance(getContext()).setLogin(true);

                            EasyConfig.getInstance().addHeader("Authorization", model.token);
                            SPManager.instance(getContext()).saveUserPhone(account);

                            // 进行内存优化，销毁所有界面
                            ActivityManager.getInstance().finishAllActivities();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
}
