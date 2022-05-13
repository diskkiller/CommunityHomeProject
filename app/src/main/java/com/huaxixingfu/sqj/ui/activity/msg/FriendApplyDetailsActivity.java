package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.ApplyFriendApi;
import com.huaxixingfu.sqj.http.api.DeApplyFriendApi;
import com.huaxixingfu.sqj.http.api.FriendListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

/**
 * @Description: java类作用描述
 */
public class FriendApplyDetailsActivity extends AppActivity {

    ImageView niv_avater;
    TextView tv_nickname;
    TextView address;
    TextView content;
    FriendListApi.Bean model;

    long chatFriendApplyId;

    public static void start(BaseActivity activity, FriendListApi.Bean model, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, FriendApplyDetailsActivity.class);
        intent.putExtra(IntentKey.OBJECT, model);
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
        return R.layout.sqj_activity_friend_apply_details;
    }

    @Override
    protected void initView() {

        tv_nickname = findViewById(R.id.tv_nickname);
        address = findViewById(R.id.address);
        niv_avater = findViewById(R.id.niv_avater);
        content = findViewById(R.id.content);

        setOnClickListener(R.id.tv_ok,R.id.tv_no);
    }

    @Override
    protected void initData() {
        model = getSerializable(IntentKey.OBJECT);
        if(model != null)
            setUserData();
    }

    private void setUserData(){
        //用户数据
        Glide.with(niv_avater)
                .load(model.userAvatarUrl)
                .placeholder(R.mipmap.icon_logo)
                .error(R.mipmap.icon_logo)
                .into(niv_avater);

        chatFriendApplyId =  model.chatFriendApplyId;
        tv_nickname.setText(StringUtils.nullChanegEmpty(model.userNickName));
        address.setText(StringUtils.nullChanegEmpty(model.zoneRoomAddr));
        content.setText(StringUtils.nullChanegEmpty(model.userSignName));

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_ok) {
            //同意
            ///sggl/mobile/chat/friend/v1.0/apply/approve
            //04【app-聊天-申请好友-(通过)】
            if(chatFriendApplyId>0){
                addFriend(chatFriendApplyId);
            }


        }

        if (id == R.id.tv_no) {
            //拒绝
            if(chatFriendApplyId>0){
                noFriend(chatFriendApplyId);
            }
        }
    }

    private void addFriend(long userid){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatFriendApplyId",userid);
        EasyHttp.post(this)
                .api(new ApplyFriendApi())
                .json(map)
                .request(new HttpCallback<HttpData<ApplyFriendApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<ApplyFriendApi.Bean> data) {
                        if(data.getData() != null){
                            boolean status = data.getData().status;
                            if(status){
                                ToastUtils.show("申请添加用户成功");
                                FriendApplyDetailsActivity.this.finish();
                            }else{
                                ToastUtils.show(data.getData().message);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void noFriend(long userid){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatFriendApplyId",userid);
        EasyHttp.post(this)
                .api(new DeApplyFriendApi())
                .json(map)
                .request(new HttpCallback<HttpData<DeApplyFriendApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<DeApplyFriendApi.Bean> data) {
                        if(data.getData() != null){
                            boolean status = data.getData().status;
                            if(status){
                                ToastUtils.show("拒绝添加用户成功");
                                FriendApplyDetailsActivity.this.finish();
                            }else{
                                ToastUtils.show(data.getData().message);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }



}
