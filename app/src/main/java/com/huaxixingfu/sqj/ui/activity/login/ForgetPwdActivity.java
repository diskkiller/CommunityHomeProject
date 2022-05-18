package com.huaxixingfu.sqj.ui.activity.login;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.view.CountdownView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.GetForgetCodeApi;
import com.huaxixingfu.sqj.http.api.PasswordApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.MatchUtils;

import java.util.HashMap;

public class ForgetPwdActivity extends AppActivity implements View.OnClickListener {

    private String account, code, pwd, confirmPwd;
    private CountdownView mTextCountTimer;
    private EditText etAccount,etCode,etPwd,etConfirmPwd;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_forget_pwd;
    }

    @Override
    protected void initView() {
        mTextCountTimer = findViewById(R.id.tv_get_code);
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);
        etPwd = findViewById(R.id.et_pwd);
        etConfirmPwd = findViewById(R.id.et_confirm_pwd);

        setOnClickListener(R.id.tv_get_code,R.id.tv_submit,R.id.iv_back);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_submit) {
            account = etAccount.getText().toString();
            code = etCode.getText().toString();
            pwd = etPwd.getText().toString();
            confirmPwd = etConfirmPwd.getText().toString();
            //判断参数情况
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(account)) {
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
            reset(account, code, pwd, confirmPwd);
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.tv_get_code) {
            account = etAccount.getText().toString();
            //判断参数情况
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            mTextCountTimer.start();
            getCode(account);
        }

    }

    private void reset(String account, String code, String password, String confirmPassword){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("code", code);
        map.put("password", password);
        map.put("confirmPassword", confirmPassword);
        EasyHttp.post(this)
                .api(new PasswordApi())
                .json(map)
                .request(new HttpCallback<HttpData<PasswordApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<PasswordApi.Bean> data) {
                        if(data.getData() != null){
                            PasswordApi.Bean model = data.getData();
                            if (!model.status) {
                                return;
                            }
                            ToastUtils.show(model.dataMessage);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getCode(String account){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        EasyHttp.post(this)
                .api(new GetForgetCodeApi())
                .json(map)
                .request(new HttpCallback<HttpData<GetForgetCodeApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<GetForgetCodeApi.Bean> data) {
                        if(data.getData() != null){
                            GetForgetCodeApi.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            ToastUtils.show("发送验证码成功");
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
}