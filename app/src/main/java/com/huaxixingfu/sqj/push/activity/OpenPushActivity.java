package com.huaxixingfu.sqj.push.activity;

import android.content.Intent;
import android.net.Uri;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.utils.AppLogMessageMgr;

/**
 * Created by lsm on 2022/5/25.
 * Describe:
 */
public class OpenPushActivity extends AppActivity {

    public static final String OPEN_HOME = "HOME";
    public static final String OPEN_NEWS = "NEWS";
    public static final String OPEN_IM = "IM";
    public static final String OPEN_VOIP = "VOIP";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_push;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        huaxixingfu://sqj/open_push?openId=HOME&showPath=http://www.qq.com
        Intent intent = getIntent();
        Uri data = intent.getData();  //
        AppLogMessageMgr.e("OpenPush", "DataString==========="+intent.getDataString());
        String openId = data.getQueryParameter("openId");
        String showPath = data.getQueryParameter("showPath");
        AppLogMessageMgr.e("OpenPush", "openId==========="+openId);
        AppLogMessageMgr.e("OpenPush", "showPath==========="+showPath);
        if (OPEN_HOME.equals(openId)){
            startActivity(new Intent(this, HomeActivity.class));
        }else if (OPEN_NEWS.equals(openId)){
            BrowserActivity.start(this, showPath);
        }else if (OPEN_IM.equals(openId)){

        }else if (OPEN_VOIP.equals(openId)){

        }
        finish();

    }
}
