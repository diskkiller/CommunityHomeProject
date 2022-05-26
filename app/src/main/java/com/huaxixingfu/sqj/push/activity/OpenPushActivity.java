package com.huaxixingfu.sqj.push.activity;

import android.content.Intent;
import android.net.Uri;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.utils.AppLogMessageMgr;

/**
 * Created by lsm on 2022/5/25.
 * Describe:
 */
public class OpenPushActivity extends AppActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_push;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        huaxixingfu://sqj/open_push?openId=HOME&webPath=http://www.qq.com
        Intent intent = getIntent();
        Uri data = intent.getData();  //
        AppLogMessageMgr.e("OpenPush", "DataString==========="+intent.getDataString());
        String openId = data.getQueryParameter("openId");
        String webPath = data.getQueryParameter("webPath");
        AppLogMessageMgr.e("OpenPush", "openId==========="+openId);
        AppLogMessageMgr.e("OpenPush", "showPath==========="+webPath);
        int anInt = -1;
        try{
            anInt = Integer.parseInt(openId);
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean matchRouter = PushScheme.matchRouter(this, anInt, webPath);
        // 如果匹配上了路由则关闭当前页面  否则直接去首页
        if (matchRouter){
            finish();
        }else {
            startActivity(new Intent(this, HomeActivity.class));
        }

    }
}
