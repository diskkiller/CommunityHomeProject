package com.huaxixingfu.sqj.ui.activity.position;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.bar.TitleBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.ADBean;
import com.huaxixingfu.sqj.http.api.ADApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;


/**
 *  政务大厅
 */
public class HallActivity extends AppActivity implements View.OnClickListener {




    @Override
    protected int getLayoutId() {
        return R.layout.sqj_position_hall_layout;
    }

    @Override
    protected void initView() {

//        TitleBar titleBar = findViewById(R.id.tb_title);
        setOnClickListener(R.id.ll_view_1_1,
                R.id.ll_view_1_2,
                R.id.ll_view_1_3,
                R.id.ll_view_1_4,
                R.id.ll_view_2_1,
                R.id.ll_view_2_2
                );

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_view_1_1:
                BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/rkgl/index",getString(R.string.postion_hall_1_1));
//                户政业务：
                break;
            case R.id.ll_view_1_2:
//                治安业务：
            BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/zagl/index",getString(R.string.postion_hall_1_2));
                break;
            case R.id.ll_view_1_3:
//                交管业务：
            BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/zwfw/ywxt/jiaoguan",getString(R.string.postion_hall_1_3));
                break;
            case R.id.ll_view_1_4:
//                监管业务：
            BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/zwfw/ywxt/jianguan",getString(R.string.postion_hall_1_4));
                break;
            case R.id.ll_view_2_1:
//                出入境业务：

            BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/crjgl/index",getString(R.string.postion_hall_2_1));
                break;
            case R.id.ll_view_2_2:
//                公交业务：
            BrowserActivity.start(this,"https://zwfw.gaj.beijing.gov.cn/zwfw/ywxt/gongjiao",getString(R.string.postion_hall_2_2));
                break;

        }
    }


}
