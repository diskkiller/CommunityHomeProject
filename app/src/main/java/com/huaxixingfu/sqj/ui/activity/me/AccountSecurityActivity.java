package com.huaxixingfu.sqj.ui.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.diskkiller.widget.layout.SettingBar;
import com.hjq.bar.TitleBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

public class AccountSecurityActivity extends AppActivity {

    private SettingBar sb_setting_phone,sb_setting_password,sb_setting_cancel_account;
    private TitleBar tb_title;
    private ImageView bar_back;


    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_account_security;
    }

    public void initView() {

        tb_title = findViewById(R.id.tb_title);
        tb_title.setTitle("账号安全");
        tb_title.getRightView().setVisibility(View.INVISIBLE);

        sb_setting_phone = findViewById(R.id.sb_setting_phone);
        sb_setting_password = findViewById(R.id.sb_setting_password);
        sb_setting_cancel_account = findViewById(R.id.sb_setting_cancel_account);

        setOnClickListener(R.id.sb_setting_phone,R.id.sb_setting_password,R.id.sb_setting_cancel_account);

        sb_setting_phone.setRightText(StringUtils.phoneNumber());

    }


    public void initData() {

    }

    /**
     * viewmodel初始化及相关监听
     */
    private void initObs() {

    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sb_setting_phone) {
            startActivity(new Intent(getActivity(), PhoneResetActivity.class));

        }else if(id == R.id.sb_setting_password){
            startActivity(new Intent(getActivity(), PasswordResetActivity.class));

        }else if(id == R.id.sb_setting_cancel_account){
            startActivity(new Intent(getActivity(), CancleAccountActivity.class));

        }
    }
}
