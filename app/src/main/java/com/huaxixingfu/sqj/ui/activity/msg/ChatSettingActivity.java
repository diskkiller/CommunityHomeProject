package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.widget.layout.SettingBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.ui.activity.me.EditeTextActivity;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.longbei.im_push_service_sdk.common.app.widget.ImageView;

/**
 * @Description: java类作用描述
 */
public class ChatSettingActivity extends AppActivity {

    TextView tv_remark;
    TextView tv_nickname;
    SettingBar sb_address;
    private long targetUid;
    private String nickName;
    private ImageView iv_rename;

    public static void start(BaseActivity activity, long targetUid, String nickName, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, ChatSettingActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
        intent.putExtra(IntentKey.NICKNAME, nickName);
        activity.startActivityForResult(intent, (resultCode, data) -> {
            if (listener == null) {
                return;
            }
            if (resultCode == RESULT_OK) {
                listener.onSucceed(data.getStringExtra(IntentKey.STRING_DATE));
            } else {
                listener.onFail();
            }
        });
    }

    public void finishForResult(String date) {
        setResult(RESULT_OK, new Intent()
                .putExtra(IntentKey.STRING_DATE, date));
        finish();
    }

    /**
     * 注册监听
     */
    public interface OnFinishResultListener {

        void onSucceed(String data);

        default void onFail() {}
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_friend_setting;
    }

    @Override
    protected void initView() {
        tv_remark = findViewById(R.id.tv_remark);
        tv_nickname = findViewById(R.id.tv_nickname);
        sb_address = findViewById(R.id.sb_address);
        iv_rename = findViewById(R.id.iv_rename);
        setOnClickListener(R.id.iv_rename);
    }

    @Override
    protected void initData() {
        targetUid = getLong(IntentKey.TARGETUID, 0);
        nickName = getString(IntentKey.NICKNAME);

        tv_remark.setText(nickName);
        tv_nickname.setText(nickName);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_chat) {
            MsgActivity.start(ChatSettingActivity.this,targetUid,
                    targetUid+"",nickName,null);
            finish();
        }else if(id == R.id.sb_edite_friend_rename){
            String name = StringUtils.isEmpty(nickName) ? "":nickName;
            EditeTextActivity.start(ChatSettingActivity.this, "昵称设置", name,20, (int) targetUid,name,new EditeTextActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {
                    //sbPersonalName.setRightText(data);
                }
            });
        }
    }
}
