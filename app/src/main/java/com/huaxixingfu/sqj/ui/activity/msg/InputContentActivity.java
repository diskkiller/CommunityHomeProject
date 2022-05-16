package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.EditFriendNameApi;
import com.huaxixingfu.sqj.http.api.GroupNoteAddApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

public class InputContentActivity extends AppActivity {

    private long targetUid;

    public static void start(BaseActivity activity, long targetUid, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, InputContentActivity.class);
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


    private EditText et_content;

    private TitleBar tb_title;

    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_input_content;
    }

    public void initView() {
        targetUid = getLong(IntentKey.TARGETUID);

        et_content = findViewById(R.id.et_content);

        setOnClickListener(R.id.tv_submit);

    }


    public void initData() {

    }

    private void editeFriendName(int groupId,String chatGroupNoticeContent){
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId",groupId);
        map.put("chatGroupNoticeContent",chatGroupNoticeContent);
        EasyHttp.post(this)
                .api(new GroupNoteAddApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        finish();
                       /* if(data.getData() != null){
                            finish();
                            //finishForResult(et_input.getText().toString());
                        }*/
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }



    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_submit){
            if(StringUtils.isNotEmpty(et_content.getText().toString())){
                editeFriendName(Integer.parseInt(targetUid+""),et_content.getText().toString());
            }else{
                ToastUtils.show("请输入内容");
            }
        }
    }
}
