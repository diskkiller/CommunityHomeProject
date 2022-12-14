package com.huaxixingfu.sqj.ui.activity.me;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.view.CountdownView;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.VCode;
import com.huaxixingfu.sqj.http.api.RealNameSubmitApi;
import com.huaxixingfu.sqj.http.api.ResetPasswordApi;
import com.huaxixingfu.sqj.http.api.ResetPasswordCodeApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.Sm4Util;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

public class PasswordResetActivity extends AppActivity {

    private EditText mFirstPassword;
    private EditText mSecondPassword;
    private EditText et_password_code;
    private CountdownView cv_password_countdown;
    private Button mCommitView;
    private TextView tv_phone;

    private TitleBar tb_title;


    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_password_reset;
    }

    public void initView() {

        tb_title = findViewById(R.id.bar_title);
        tb_title.getRightView().setVisibility(View.INVISIBLE);

        tv_phone = findViewById(R.id.tv_phone);
        cv_password_countdown = findViewById(R.id.cv_password_countdown);
        mFirstPassword = findViewById(R.id.et_password_reset_password1);
        mSecondPassword = findViewById(R.id.et_password_reset_password2);
        et_password_code = findViewById(R.id.et_password_code);
        mCommitView = findViewById(R.id.btn_password_commit);
        setOnClickListener(mCommitView,cv_password_countdown);
    }


    public void initData() {
        tv_phone.setText(StringUtils.phoneNumber());
    }

    private void resetPasswordCode(){
        EasyHttp.post(this)
                .api(new ResetPasswordCodeApi())
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                cv_password_countdown.start();
                                ToastUtils.show("????????????");
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void resetPassWord(String phone,String code,String password,String confirmPassword){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", phone);
        map.put("code", code);
        map.put("password", password);
        map.put("confirmPassword", confirmPassword);
        EasyHttp.post(this)
                .api(new ResetPasswordApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("????????????");
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_password_commit) {

            String phone = SPManager.instance(getActivity()).getUserPhone();
            String code = et_password_code.getText().toString();
            String firstPassword = mFirstPassword.getText().toString();
            String secondPassword = mSecondPassword.getText().toString();

            if(StringUtils.isEmpty(firstPassword) || StringUtils.isEmpty(secondPassword)){
                ToastUtils.show("???????????????");
                return;
            }
            if (!firstPassword.equals(secondPassword)) {
                ToastUtils.show("??????????????????????????????");
                return;
            }


            String cipher = "";
            try {
                System.out.println("??????****************************");
                System.out.println("????????????"+firstPassword);
                cipher = Sm4Util.encryptEcb(Sm4Util.KEY,firstPassword);//sm4??????
                System.out.println("????????????"+cipher);
                System.out.println("?????????"+Sm4Util.verifyEcb(Sm4Util.KEY,cipher,firstPassword));//???????????????????????????????????????
            } catch (Exception e) {
                e.printStackTrace();
            }

            resetPassWord(phone,code,cipher.toUpperCase(),cipher.toUpperCase());
        }else if(id == R.id.cv_password_countdown){

            resetPasswordCode();
        }
    }
}
