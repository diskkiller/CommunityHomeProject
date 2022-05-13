package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.SearchFriendApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.shehuan.niv.NiceImageView;

import java.util.HashMap;

/**
 * @Description: 搜索好友
 */
public class SearchFriendActivity extends AppActivity {

    EditText et_text;
    TextView tv_send;
    RelativeLayout rl_result;
    NiceImageView niv_avater;
    TextView tv_nickname;
    TextView tv_content;
    private String userImgUrl,userName;
    private long userId;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_search_friend;
    }

    @Override
    protected void initView() {
        et_text = findViewById(R.id.et_text);
        tv_send = findViewById(R.id.tv_send);
        rl_result = findViewById(R.id.rl_result);//如果搜索到结果,此处可以展示,若无结果,隐藏
        //若有搜索结果,请设置以下控件的数据
        niv_avater = findViewById(R.id.niv_avater);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_content = findViewById(R.id.tv_content);

        setOnClickListener(R.id.tv_send,R.id.tv_see);

        initET();
    }

    @Override
    protected void initData() {

    }

    private void searchFriend(String userPhone){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userPhone",userPhone);
        EasyHttp.post(this)
                .api(new SearchFriendApi())
                .json(map)
                .request(new HttpCallback<HttpData<SearchFriendApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<SearchFriendApi.Bean> data) {
                        if(data.getData() != null){
                            rl_result.setVisibility(View.VISIBLE);

                            userImgUrl = data.getData().userAvatarUrl;
                            userId = data.getData().userId;
                            userName = data.getData().userNickName;
                            //用户数据
                            Glide.with(niv_avater)
                                    .load(userImgUrl)
                                    .placeholder(R.mipmap.icon_logo)
                                    .error(R.mipmap.icon_logo)
                                    .into(niv_avater);

                            tv_nickname.setText(data.getData().userNickName);
                            tv_content.setText(data.getData().userSignName);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_send){
            String phoen = et_text.getText().toString().trim();
            if((null == phoen) || (phoen.length()== 0)){
                ToastUtils.show("请输入查找电话");
                return;
            }

            if(phoen.length() != 11){
                ToastUtils.show("请输入正确电话");
                return;
            }
            searchFriend(phoen);
        }


        if(id == R.id.tv_see){
            AddFriendApplyActivity.start(SearchFriendActivity.this,userId,userName,userImgUrl,null);
        }
    }

    /**
     * 编辑聊天内容的初始化，最大限制五百字
     */
    private void initET() {
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_send.setVisibility(View.VISIBLE);
                } else {
                    tv_send.setVisibility(View.GONE);
                }
            }
        });
    }
}
