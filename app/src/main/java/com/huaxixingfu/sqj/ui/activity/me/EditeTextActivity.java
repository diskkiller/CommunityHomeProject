package com.huaxixingfu.sqj.ui.activity.me;

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
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.EditFriendNameApi;
import com.huaxixingfu.sqj.http.api.MsgNotesListEditeApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.HashMap;

public class EditeTextActivity extends AppActivity {


    public static void start(BaseActivity activity, String title, String text, int maxInput,int userId,String name, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, EditeTextActivity.class);
        intent.putExtra(IntentKey.TITLE, title);
        intent.putExtra(IntentKey.TEXT, text);
        intent.putExtra(IntentKey.COUNT, maxInput);
        intent.putExtra(IntentKey.ID, userId);
        intent.putExtra(IntentKey.NICKNAME, name);
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


    private EditText et_input;

    private TitleBar tb_title;

    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_input_text;
    }

    public void initView() {
        String title = getString(IntentKey.TITLE);
        String text = getString(IntentKey.TEXT);
        int maxInput = getInt(IntentKey.COUNT);
        tb_title = findViewById(R.id.tb_title);
        tb_title.setTitle(title);

        et_input = findViewById(R.id.et_input);
        if(StringUtils.isNotEmpty(text))
            et_input.setText(text);
        if(maxInput != 0){
            //手动设置maxLength为10
            InputFilter[] filters = {new InputFilter.LengthFilter(maxInput)};
            et_input.setFilters(filters);
        }
    }

    @Override
    public void onRightClick(View view) {
        if(StringUtils.isNotEmpty(et_input.getText().toString())){
            editeFriendName(getInt(IntentKey.ID),getString(IntentKey.NICKNAME));
        }else{
            ToastUtils.show("请输入内容");
        }
    }

    private void editeFriendName(int chatToUserId,String chatFriendNiceName){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatToUserId",chatToUserId);
        map.put("chatFriendNiceName",chatFriendNiceName);
        EasyHttp.post(this)
                .api(new EditFriendNameApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        if(data.getData() != null){

                            finishForResult(et_input.getText().toString());
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    public void initData() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
