package com.huaxixingfu.sqj.ui.activity.me;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.diskkiller.widget.view.ClearEditText;
import com.diskkiller.widget.view.CountdownView;
import com.diskkiller.widget.view.RegexEditText;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.VCode;
import com.huaxixingfu.sqj.http.api.ResetPhoneApi;
import com.huaxixingfu.sqj.http.api.ResetPhoneCodeApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

public class PhoneResetActivity extends AppActivity {

    private static final String TAG = PhoneResetActivity.class.getSimpleName();

    private String editPhone;

    private CountdownView cvPhoneResetCountdown;
    private AppCompatButton btnPhoneResetCommit;
    private SettingBar sbSettingPhone;
    private RegexEditText etPhoneResetPhone;
    private ClearEditText etPhoneResetCode;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_phone_reset;
    }

    public void initView() {

        cvPhoneResetCountdown = findViewById(R.id.cv_phone_reset_countdown);
        btnPhoneResetCommit = findViewById(R.id.btn_phone_reset_commit);
        sbSettingPhone = findViewById(R.id.sb_setting_phone);
        etPhoneResetPhone = findViewById(R.id.et_phone_reset_phone);
        etPhoneResetCode = findViewById(R.id.et_phone_reset_code);
        sbSettingPhone.setRightText(StringUtils.phoneNumber());
        setOnClickListener(R.id.cv_phone_reset_countdown,R.id.btn_phone_reset_commit);
    }


    public void initData() {

    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cv_phone_reset_countdown:

                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    ToastUtils.show("手机号输入不正确");
                    return;
                }

                resetPoneCode(etPhoneResetPhone.getText().toString());


                break;
            case R.id.btn_phone_reset_commit:
                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    ToastUtils.show("手机号输入不正确");
                    return;
                }
                resetPoneNum(etPhoneResetPhone.getText().toString(),etPhoneResetCode.getText().toString());

                break;

        }
    }

    private void resetPoneCode(String phone){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", phone);
        EasyHttp.post(this)
                .api(new ResetPhoneCodeApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("发送成功");
                                cvPhoneResetCountdown.start();
                            }else{
                                if("手机号已存在".equals(model.message)){
                                    ToastUtils.show("手机号已注册，请重新输入");
                                }else{
                                    ToastUtils.show("发送失败");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void resetPoneNum(String phone,String code){
        HashMap<String, Object> map = new HashMap<>();
        map.put("account", phone);
        map.put("code", code);
        EasyHttp.post(this)
                .api(new ResetPhoneApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        if(data.getData() != null){
                            VCode model = data.getData();
                            if(model.status){
                                ToastUtils.show("设置成功");
                                editPhone = etPhoneResetPhone.getText().toString();
                                SPManager.instance(getContext()).saveUserPhone(editPhone);
                            }else{
                                ToastUtils.show("设置失败");
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
