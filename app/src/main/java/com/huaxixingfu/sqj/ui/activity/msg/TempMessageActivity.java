package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.huaxixingfu.sqj.app.AppActivity;
import com.longbei.im_push_service_sdk.R;
import com.longbei.im_push_service_sdk.app.fragment.message.ChatGroupFragment;
import com.longbei.im_push_service_sdk.common.Common;
import com.longbei.im_push_service_sdk.common.app.Activity;
import com.longbei.im_push_service_sdk.common.app.Fragment;
import com.longbei.im_push_service_sdk.common.basepercenter.model.Author;
import com.longbei.im_push_service_sdk.im.db.Group;
import com.longbei.im_push_service_sdk.im.db.Message;
import com.longbei.im_push_service_sdk.im.db.Session;

public class TempMessageActivity extends AppActivity {
    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;
    private View tootView;
    private ImageView iv_background;

    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     *
     */
    public static void show(Context context) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TempMessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, "");
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        setTitle("");

        mReceiverId = getString(KEY_RECEIVER_ID);
        mIsGroup = getBoolean(KEY_RECEIVER_IS_GROUP);

        Fragment fragment;
        if (mIsGroup){
            fragment = new ChatGroupFragment();
        }
        else
            fragment = new TempChatUserFragment();

        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }

    @Override
    protected void initData() {

    }
}
