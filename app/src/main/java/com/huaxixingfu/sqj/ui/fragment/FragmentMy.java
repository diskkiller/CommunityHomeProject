package com.huaxixingfu.sqj.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.PersonalDataApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.manager.ActivityManager;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.me.AccountSecurityActivity;
import com.huaxixingfu.sqj.ui.activity.me.FamilyDateActivity;
import com.huaxixingfu.sqj.ui.activity.me.FeedbackActivity;
import com.huaxixingfu.sqj.ui.activity.me.PersonalDataActivity;
import com.huaxixingfu.sqj.ui.activity.me.SettingActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;

public class FragmentMy extends TitleBarFragment<HomeActivity> {

    private PersonDataBean personDataBean;

    public static FragmentMy newInstance() {
        return new FragmentMy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_my;
    }

    private void getPersonDate(){
        EasyHttp.post(this)
                .api(new PersonalDataApi())
                .request(new HttpCallback<HttpData<PersonDataBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<PersonDataBean> data) {
                        if(data.getData() != null){
                            personDataBean = data.getData();
                            setUserDate();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private LinearLayout llHeader;
    private TextView tvUserDate,tvLogout,tvTitle;
    private SettingBar user_setting_family,user_account_security,user_feedback,user_setting;
    private ImageView icIcon;

    public void initView() {
        llHeader = findViewById(R.id.ll_header);
        tvUserDate = findViewById(R.id.tv_user_date);
        tvLogout = findViewById(R.id.tv_logout);
        tvTitle = findViewById(R.id.tv_title);
        icIcon = findViewById(R.id.ic_icon);
        user_setting_family = findViewById(R.id.sb_user_setting_family);
        user_account_security = findViewById(R.id.sb_user_account_security);
        user_feedback = findViewById(R.id.sb_user_feedback);
        user_setting = findViewById(R.id.sb_user_setting);

       setOnClickListener(R.id.ll_header,R.id.tv_user_date,
               R.id.tv_logout,R.id.sb_user_setting_family,
               R.id.sb_user_account_security,R.id.sb_user_feedback,R.id.sb_user_setting);
    }

    @Override
    protected void initData() {
        getPersonDate();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(SPManager.instance(getContext()).isLogin()){
            getPersonDate();
        }else{
            tvTitle.setText("未登录");
            tvUserDate.setVisibility(View.GONE);
            tvLogout.setVisibility(View.GONE);
            Glide.with(getActivity()).load(R.drawable.avatar_placeholder_ic).into(icIcon);
        }
    }


    private void setUserDate(){
        if(personDataBean != null ){
            SPManager.instance(getActivity()).putModel(Constants.USERINFO,personDataBean);
            if(StringUtils.isNotEmpty(personDataBean.getUserAvatarUrl()))
                Glide.with(getActivity()).load(personDataBean.getUserAvatarUrl()).into(icIcon);
            tvTitle.setText(personDataBean.getUserNickName());
            tvUserDate.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
        }
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId != R.id.sb_user_setting && !SPManager.instance(getContext()).isLogin()){
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }

        if (viewId == R.id.tv_user_date) {

            startActivity(new Intent(getActivity(), PersonalDataActivity.class));

        }else if (viewId == R.id.sb_user_setting_family) {
            startActivity(new Intent(getActivity(), FamilyDateActivity.class));

        }else if(viewId == R.id.sb_user_account_security){
            startActivity(new Intent(getActivity(), AccountSecurityActivity.class));

        }else if(viewId == R.id.sb_user_feedback){
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
        }else if(viewId == R.id.sb_user_setting){
            startActivity(new Intent(getActivity(), SettingActivity.class));

        }else if(viewId == R.id.tv_logout){
            ActivityManager.getInstance().loginOut(getActivity());
        }
    }

}
