package com.huaxixingfu.sqj.ui.activity.me;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.view.CountdownView;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.LogoutCodeApi;
import com.huaxixingfu.sqj.http.api.LogoutSubmitApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

public class CancleAccountActivity extends AppActivity {

    private Button btn_request,btn_cancle;
    private RelativeLayout rl_dialog_bg;

    private  TextView tv_ui_title;
    private  TextView mPhoneView;
    private  EditText mCodeView;
    private CountdownView mCountdownView;

    private  TextView mCancelView,tv_ui_cancel_confirm;
    private  TextView mConfirmView,tv_ui_confirm_confirm;

    private LinearLayout ll_content;
    private CardView card_deconfirm,card_confirm;

    /** 当前手机号 */
    private  String mPhoneNumber;
    private TextView tv_yinsi,tv_yonghu,tv_account;

    private CheckBox check_xieyi;

    private boolean isAgree = false;

    private TitleBar tb_title;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_cancle_account;
    }

    public void initView() {

        tb_title = findViewById(R.id.tb_title);
        tb_title.setTitle("账号注销");
        tb_title.getRightView().setVisibility(View.INVISIBLE);

        tv_ui_title = findViewById(R.id.tv_ui_title);
        tv_account = findViewById(R.id.tv_account);
        btn_cancle = findViewById(R.id.btn_cancle);
        btn_request = findViewById(R.id.btn_request);
        rl_dialog_bg = findViewById(R.id.rl_dialog_bg);
        ll_content = findViewById(R.id.ll_content);

        card_confirm = findViewById(R.id.card_confirm);
        card_deconfirm = findViewById(R.id.card_deconfirm);

        mPhoneView = findViewById(R.id.tv_safe_phone);
        mCodeView = findViewById(R.id.et_safe_code);
        mCountdownView = findViewById(R.id.cv_safe_countdown);

        mCancelView  = findViewById(R.id.tv_ui_cancel);
        mConfirmView  = findViewById(R.id.tv_ui_confirm);

        tv_ui_cancel_confirm  = findViewById(R.id.tv_ui_cancel_confirm);
        tv_ui_confirm_confirm  = findViewById(R.id.tv_ui_confirm_confirm);

        tv_yinsi  = findViewById(R.id.tv_yinsi);
        tv_yonghu  = findViewById(R.id.tv_yonghu);
        check_xieyi = findViewById(R.id.check_xieyi);

        mPhoneNumber = SPManager.instance(this).getUserPhone();
        // 为了保护用户的隐私，不明文显示中间四个数字
        mPhoneView.setText(StringUtils.phoneNumber());
        tv_account.setText("注销账号："+StringUtils.phoneNumber());


        setOnClickListener(btn_cancle,btn_request,mCountdownView,mCancelView,
                mConfirmView,tv_ui_confirm_confirm,tv_yinsi,tv_yonghu,check_xieyi);
    }


    public void initData() {
    }

    private void reqestLogoutCode(){
        EasyHttp.post(this)
                .api(new LogoutCodeApi())
                .request(new HttpCallback<HttpData<LogoutCodeApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<LogoutCodeApi.Bean> data) {
                        if(data.getData() != null){
                            LogoutCodeApi.Bean model = data.getData();
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
    private void requestLogoutSubmit(String code){
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        EasyHttp.post(this)
                .api(new LogoutSubmitApi())
                .json(map)
                .request(new HttpCallback<HttpData<LogoutSubmitApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<LogoutSubmitApi.Bean> data) {
                        if(data.getData() != null){
                            LogoutSubmitApi.Bean model = data.getData();
                            if (!model.status) {
                                ToastUtils.show(model.message);
                                return;
                            }
                            ToastUtils.show("注销成功");
                            ActivityManager.getInstance().loginOut(getActivity());
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
        if(id == R.id.btn_cancle) {
            finish();
        }else if(id == R.id.btn_request){
            if (!isAgree) {
                ToastUtils.show(R.string.agree_user_contract);
                return;
            }
            rl_dialog_bg.setVisibility(View.VISIBLE);
            card_deconfirm.setVisibility(View.VISIBLE);
            card_confirm.setVisibility(View.GONE);

        }else if (id == R.id.cv_safe_countdown) {
            // 验证码校验
            reqestLogoutCode();
        } else if (id == R.id.tv_ui_confirm) {

            if (mCodeView.getText().toString().length() != 6) {
                ToastUtils.show(R.string.common_code_error_hint);
                return;
            }
            card_deconfirm.setVisibility(View.GONE);
            card_confirm.setVisibility(View.VISIBLE);


        }else if (id == R.id.tv_ui_confirm_confirm) {

            requestLogoutSubmit(mCodeView.getText().toString());

        } else if (id == R.id.tv_ui_cancel || id == R.id.tv_ui_cancel_confirm) {
            rl_dialog_bg.setVisibility(View.GONE);
        }else if (id == R.id.tv_yinsi) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getPrivate());
        } else if (id == R.id.tv_yonghu) {
            BrowserActivity.start(getActivity(), SPManager.instance(getActivity()).getAgreement());
        }else if(id == R.id.check_xieyi){
            isAgree = !isAgree;
        }
    }
}
