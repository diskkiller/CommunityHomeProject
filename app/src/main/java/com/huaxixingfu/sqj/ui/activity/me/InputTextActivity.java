package com.huaxixingfu.sqj.ui.activity.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.diskkiller.base.BaseActivity;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.utils.StringUtils;

public class InputTextActivity extends AppActivity {


    public static void start(BaseActivity activity, String title, String text, int maxInput, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, InputTextActivity.class);
        intent.putExtra(IntentKey.TITLE, title);
        intent.putExtra(IntentKey.TEXT, text);
        intent.putExtra(IntentKey.COUNT, maxInput);
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
        switch (title){

            case "输入姓名":
            case "输入身份证号":
                et_input.setHint("请"+title);
                break;
            case "职业信息":
            case "个性签名":
                et_input.setHint("请输入"+title);
                break;
        }

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
            finishForResult(et_input.getText().toString());
        }else{
            ToastUtils.show("请输入内容");
        }
    }

    public void initData() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
