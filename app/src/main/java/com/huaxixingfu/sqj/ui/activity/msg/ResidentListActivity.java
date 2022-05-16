package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.msg.CreatGroutApi;
import com.huaxixingfu.sqj.http.api.msg.ResidentListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.ResidentListAdapter;
import com.huaxixingfu.sqj.utils.GsonUtil;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：lblbh on 2022/4/30 20:38
 * 邮箱：lanbuhan@163.com
 * 功能：社工列表
 */

public class ResidentListActivity extends AppActivity implements
        OnItemClickListener {

    ResidentListAdapter mAdapter;

    RecyclerView recycler;

    List<ResidentListApi.Bean> model;
    private ArrayList<String> selMemberIdList = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_resident_list;
    }

    @Override
    protected void initView() {
        setTitle("专属社工");
        recycler = findViewById(R.id.recycler);
        mAdapter = new ResidentListAdapter(R.layout.sqj_item_shegong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    public void initData(){
        getResidentList();
    }

    private void getResidentList(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","");
        map.put("groupId",0);
        EasyHttp.post(this)
                .api(new ResidentListApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<ResidentListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<ResidentListApi.Bean>> data) {
                        if(data.getData() != null){
                            model = data.getData();
                            if(null != model && model.size() > 0) {
                                mAdapter.setList(model);
                            }

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private String memberId = "";
    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {


        ChatSettingActivity.start(ResidentListActivity.this,mAdapter.getData().get(position).getUserId(),
                mAdapter.getData().get(position).getResidentNickName(),null);

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

    }


}
