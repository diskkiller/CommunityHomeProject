package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;

public class TempMessageActivity extends AppActivity {
    // 接收者Id，可以是群，也可以是人的Id
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    // 是否是群
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;
    private View tootView;
    private ImageView iv_background;

    private long targetUid;
    private String sessionId;
    private String nickName;

    /**
     * 通过Session发起聊天
     *
     * @param context 上下文
     *
     */
    public static void show(Context context,long targetUid,String sessionId, String nickName,boolean mIsGroup) {
        if (context == null)
            return;
        Intent intent = new Intent(context, TempMessageActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
        intent.putExtra(IntentKey.SESSIONID, sessionId);
        intent.putExtra(IntentKey.NICKNAME, nickName);
        intent.putExtra(IntentKey.KEY_IS_GROUP, mIsGroup);
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

        targetUid = getLong(IntentKey.TARGETUID, 0);
        sessionId = getString(IntentKey.SESSIONID);
        nickName = getString(IntentKey.NICKNAME);
        mIsGroup = getBoolean(IntentKey.KEY_IS_GROUP);

        Fragment fragment;
       /* if (mIsGroup){
            fragment = new ChatGroupFragment();
        }
        else*/
        fragment = new TempChatUserFragment();

        // 从Activity传递参数到Fragment中去
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        bundle.putLong(IntentKey.TARGETUID, targetUid);
        bundle.putString(IntentKey.SESSIONID, sessionId);
        bundle.putString(IntentKey.NICKNAME, nickName);
        bundle.putBoolean(IntentKey.KEY_IS_GROUP, mIsGroup);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container, fragment)
                .commit();
    }

    @Override
    protected void initData() {

    }
}
