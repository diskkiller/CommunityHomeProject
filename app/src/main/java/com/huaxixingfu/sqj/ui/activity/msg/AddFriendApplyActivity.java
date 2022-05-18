package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.RequestFriendApi;
import com.huaxixingfu.sqj.http.glide.GlideApp;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;
import java.util.HashMap;

public class AddFriendApplyActivity extends AppActivity {

    ImageView niv_avater;
    EditText et_text;
    TextView tv_nickname;
    long userId;

    public static void start(BaseActivity activity, long id, String name, String image,OnFinishResultListener listener) {
        Intent intent = new Intent(activity, AddFriendApplyActivity.class);
        intent.putExtra(IntentKey.ID, id);
        intent.putExtra(IntentKey.NAME, name);
        intent.putExtra(IntentKey.IMAGE, image);
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
        return R.layout.sqj_activity_add_friend_apply;
    }

    @Override
    protected void initView() {
        niv_avater = findViewById(R.id.niv_avater);
        tv_nickname = findViewById(R.id.tv_nickname);
        et_text = findViewById(R.id.et_text);
        setOnClickListener(R.id.tv_submit);
    }

    @Override
    protected void initData() {
        userId = getLong(IntentKey.ID);
        GlideApp.with(getContext())
                .load(getString(IntentKey.IMAGE))
                .into(niv_avater);
        if(StringUtils.isNotEmpty(getString(IntentKey.NAME)))
            tv_nickname.setText(getString(IntentKey.NAME));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_submit) {
            if(userId <=0)return;
            String desc = et_text.getText().toString().trim();
            String temp = "";
            if((null != desc) && (desc.length() >0)){
                if(!"请输入申请描述...".equals(desc)){
                    temp = desc;
                }
            }

            requestFriend(userId,temp);
        }
    }

    private void requestFriend(long userid,String chatFriendApplyRemark){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatToUserId",userid);
        map.put("chatFriendApplyRemark",chatFriendApplyRemark);
        EasyHttp.post(this)
                .api(new RequestFriendApi())
                .json(map)
                .request(new HttpCallback<HttpData<RequestFriendApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<RequestFriendApi.Bean> data) {
                        if(data.getData() != null){
                            boolean status = data.getData().status;
                            if(status){
                                ToastUtils.show("申请添加好友成功");
                                AddFriendApplyActivity.this.finish();
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