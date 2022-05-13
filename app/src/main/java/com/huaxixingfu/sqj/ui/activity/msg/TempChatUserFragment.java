package com.huaxixingfu.sqj.ui.activity.msg;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import java.io.File;

/**
 * 用户聊天界面
 */
public class TempChatUserFragment extends TempChatFragment{
    /*@BindView(R2.id.im_portrait)
    PortraitView mPortrait;*/

    private MenuItem mUserInfoMenuItem;

    public TempChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }


    // 进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSendGallery(String[] paths) {

    }

    @Override
    public void onGotoGallery() {

    }

    @Override
    public void onGotoCamera() {

    }

    @Override
    public void onRecordDone(File file, long time) {

    }
}
