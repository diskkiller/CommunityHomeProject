package com.huaxixingfu.sqj.ui.activity.msg;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.FriendListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.NewFriendAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class NewFriendsActivity extends AppActivity implements View.OnClickListener {

    private RecyclerView rv_new_friends;
    private NewFriendAdapter friendAdapter;
    private int page = 0;
    private SmartRefreshLayout srl_new_friends;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_new_friends;
    }

    @Override
    protected void initView() {
        rv_new_friends = findViewById(R.id.rv_new_friends);
        srl_new_friends = findViewById(R.id.srl_new_friends);
        initRV();
    }

    @Override
    public void onRightClick(View view) {
        startActivity(new Intent(getContext(), SearchFriendActivity.class));
    }

    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getFriendList();
    }

    private void getFriendList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        EasyHttp.post(this)
                .api(new FriendListApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<FriendListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<FriendListApi.Bean>> data) {
                        if(data.getData() != null){
                            if(friendAdapter != null && friendAdapter.getData() != null){
                                friendAdapter.getData().clear();
                                friendAdapter.notifyDataSetChanged();
                            }
                            if((null != data.getData()) && (data.getData().size() >0)){
                                friendAdapter.setData(data.getData());
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private void initRV() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_new_friends.setLayoutManager(llm);
        friendAdapter = new NewFriendAdapter(getContext());

        friendAdapter.setOnChildClickListener(R.id.tv_see,new BaseAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(RecyclerView recyclerView, View childView, int position) {
                if(childView.getId() == R.id.tv_see){
                    FriendListApi.Bean temp= friendAdapter.getItem(position);
                    FriendApplyDetailsActivity.start(NewFriendsActivity.this,temp,null);
                }
            }
        });
        rv_new_friends.setAdapter(friendAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}
