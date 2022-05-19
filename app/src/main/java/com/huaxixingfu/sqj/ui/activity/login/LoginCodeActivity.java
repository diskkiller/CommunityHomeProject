package com.huaxixingfu.sqj.ui.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.diskkiller.http.EasyConfig;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.view.CountdownView;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.GetLoginCodeApi;
import com.huaxixingfu.sqj.http.api.LoginApiByCode;
import com.huaxixingfu.sqj.http.api.LoginPhoneValidateApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.ViewUtils;

import java.util.HashMap;

public class LoginCodeActivity extends AppActivity implements View.OnClickListener {
    private String account, code;
    private TextView mGetCode;
    private CountdownView textCountTimer;
    private CheckBox checkContract;
    private EditText etAccount,etCode;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_login_code;
    }

    @Override
    protected void initView() {
        textCountTimer = findViewById(R.id.tv_get_code);
        checkContract = findViewById(R.id.check_xieyi);
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);

        TextView privateNtn = findViewById(R.id.private_btn);
        TextView aboutNtn = findViewById(R.id.about_btn);
        ViewUtils.setAntiAlias(privateNtn);
        ViewUtils.setAntiAlias(aboutNtn);

        setOnClickListener(R.id.tv_get_code,R.id.tv_tourist,
                R.id.tv_pwd_login,R.id.tv_submit,R.id.tv_register,R.id.about_btn,R.id.private_btn);
    }

    @Override
    protected void initData() {}

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_get_code) {
            //判断参数情况
            account = etAccount.getText().toString();
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            getCode(account);
        } else if (id == R.id.tv_submit) {
            //调用登录
            //判断参数情况
            account = etAccount.getText().toString();
            code = etCode.getText().toString();
            if (TextUtils.isEmpty(account)) {
                ToastUtils.show(R.string.account_error);
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(R.string.input_code_hint);
                return;
            }
            if (!checkContract.isChecked()) {
                ToastUtils.show(R.string.agree_user_contract);
                return;
            }

            login(account, code);
        } else if (id == R.id.tv_register) {
            //调用注册
            startActivity(new Intent(getActivity(), RegisterActivity.class));
        } else if (id == R.id.tv_pwd_login) {
            //密码登录
            startActivity(new Intent(getActivity(), LoginActivity.class));
            finish();
        } else if (id == R.id.tv_tourist) {
            // 进行内存优化，销毁所有界面
            ActivityManager.getInstance().finishAllActivities();
            startActivity(new Intent(getActivity(), HomeActivity.class));
        } else if (id == R.id.private_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());
        } else if (id == R.id.about_btn) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
        }
    }

    private void getCode(String account){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        EasyHttp.post(this)
                .api(new GetLoginCodeApi())
                .json(map)
                .request(new HttpCallback<HttpData<GetLoginCodeApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<GetLoginCodeApi.Bean> data) {
                        if(data.getData() != null){
                            GetLoginCodeApi.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            textCountTimer.start();
                            ToastUtils.show("发送验证码成功");
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void login(String account, String code) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("code", code);
        EasyHttp.post(this)
                .api(new LoginApiByCode())
                .json(map)
                .request(new HttpCallback<HttpData<LoginApiByCode.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<LoginApiByCode.Bean> data) {
                        if(data.getData() != null){
                            LoginApiByCode.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            //更新token
                            SPManager.instance(getContext()).setToken(model.token);
                            //更新用户
                            SPManager.instance(getApplicationContext()).putModel(Constants.USERDATA, model);
                            SPManager.instance(getApplicationContext()).setLogin(true);

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