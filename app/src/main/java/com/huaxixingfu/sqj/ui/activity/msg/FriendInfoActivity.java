package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.FriendDetailBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.ChatFriendDetailApi;
import com.huaxixingfu.sqj.http.api.EditFriendNameApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.EditeTextActivity;
import com.huaxixingfu.sqj.ui.activity.me.InputTextActivity;
import com.huaxixingfu.sqj.ui.activity.me.PersonalDataActivity;
import com.huaxixingfu.sqj.ui.dialog.InputDialog;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.shehuan.niv.NiceImageView;

import java.util.HashMap;

/**
 * @Description: java类作用描述
 */
public class FriendInfoActivity extends AppActivity {

    TextView tv_nickname;
    TextView tv_friend_nickname;
    TextView tv_address;
    private long targetUid;
    private String nickName,address,chatFriendNickname;
    private SettingBar sb_edite_friend_rename;
    NiceImageView niv_avater;
    private LinearLayout ll_friend_nickname;

    public static void start(BaseActivity activity, long targetUid,OnFinishResultListener listener) {
        Intent intent = new Intent(activity, FriendInfoActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
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
        return R.layout.sqj_activity_friend_info;
    }

    @Override
    protected void initView() {
        niv_avater = findViewById(R.id.niv_avater);
        tv_friend_nickname = findViewById(R.id.tv_friend_nickname);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_address = findViewById(R.id.tv_address);
        sb_edite_friend_rename = findViewById(R.id.sb_edite_friend_rename);
        ll_friend_nickname = findViewById(R.id.ll_friend_nickname);
        setOnClickListener(R.id.tv_chat,R.id.sb_edite_friend_rename);
    }

    @Override
    protected void initData() {
        targetUid = getLong(IntentKey.TARGETUID, 0);
        getFrienDetail();
    }

    private void getFrienDetail(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatToUserId",targetUid);
        EasyHttp.post(this)
                .api(new ChatFriendDetailApi())
                .json(map)
                .request(new HttpCallback<HttpData<FriendDetailBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<FriendDetailBean> data) {
                        if(data.getData() != null){
                            setFrindData(data.getData());
                        }

                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void setFrindData(FriendDetailBean data) {

        GlideApp.with(getContext())
                .load(data.userAvatarUrl)
                .into(niv_avater);

        if(data.userType == 3){
            ll_friend_nickname.setVisibility(View.GONE);
            tv_nickname.setText(data.userName);
            sb_edite_friend_rename.setVisibility(View.GONE);
            tv_address.setVisibility(View.GONE);
        }else{
            ll_friend_nickname.setVisibility(View.VISIBLE);
            sb_edite_friend_rename.setVisibility(View.VISIBLE);
            tv_address.setVisibility(View.VISIBLE);
            tv_nickname.setText(data.userNickName);
            if(StringUtils.isNotEmpty(data.chatFriendNiceName)){
                tv_friend_nickname.setText(data.chatFriendNiceName);
            }

            if(StringUtils.isNotEmpty(data.zoneRoomAddr)){
                tv_address.setText(data.zoneRoomAddr);
            }

        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_chat) {
            nickName = tv_nickname.getText().toString();
            String name = StringUtils.isEmpty(nickName) ? "":nickName;
            TempMessageActivity.show(getContext(),targetUid,
                    targetUid+"",name,false);
            finish();
        }else if(id == R.id.sb_edite_friend_rename){
            chatFriendNickname = tv_friend_nickname.getText().toString();
            String name = StringUtils.isEmpty(chatFriendNickname) ? "":chatFriendNickname;

            new InputDialog.Builder(getContext())
                    .setTitle("设置备注")
                    .setContent(name)
                    .setHint("请输入备注信息")
                    .setCancelable(false)
                    .setListener(new InputDialog.OnListener() {

                        @Override
                        public void onConfirm(BaseDialog dialog, String content) {
                            if(StringUtils.isNotEmpty(content))
                                editeFriendName(content);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {

                        }
                    })
                    .show();


            /*EditeTextActivity.start(FriendInfoActivity.this, "昵称设置", name,20, (int) targetUid,name,new EditeTextActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {
                    //sbPersonalName.setRightText(data);
                    sb_edite_friend_rename.setRightText(data);
                }
            });*/
        }
    }

    private void editeFriendName(String chatFriendNiceName){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatToUserId",targetUid);
        map.put("chatFriendNiceName",chatFriendNiceName);
        EasyHttp.post(this)
                .api(new EditFriendNameApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        toast("设置成功");
                        tv_friend_nickname.setText(chatFriendNiceName);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

}
