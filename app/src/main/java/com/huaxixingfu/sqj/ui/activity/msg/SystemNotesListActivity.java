package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.VNotes;
import com.huaxixingfu.sqj.bean.VNotiesData;
import com.huaxixingfu.sqj.http.api.ApplyFriendApi;
import com.huaxixingfu.sqj.http.api.MsgNotesListEditeApi;
import com.huaxixingfu.sqj.http.api.SysNotesListApi;
import com.huaxixingfu.sqj.http.api.SysNotesUpdateApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.MsgNotesAdapter;
import com.huaxixingfu.sqj.ui.adapter.SystemNotesAdapter;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：lblbh on 2022/4/30 20:38
 * 邮箱：lanbuhan@163.com
 * 功能：系统通知列表
 */

public class SystemNotesListActivity extends AppActivity implements OnRefreshLoadMoreListener,
        OnItemClickListener {

    SystemNotesAdapter adapter;

    RecyclerView recycler;

    int page = 0;

    private SmartRefreshLayout srlNewFriends;


    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_system_notes_list;
    }

    @Override
    protected void initView() {
        srlNewFriends = findViewById(R.id.srl_new_friends);
        recycler = findViewById(R.id.recycler);

        adapter = new SystemNotesAdapter(R.layout.sqj_activity_system_notes_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        srlNewFriends.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }

    public void initData(){
        getNewsColumn(10,page,false);
    }

    private void getNewsColumn(int size,int page,boolean isLoadMore){
        HashMap<String, Object> map = new HashMap<>();
        map.put("size",size);
        map.put("page",page);
        map.put("id","");
        EasyHttp.post(this)
                .api(new SysNotesListApi())
                .json(map)
                .request(new HttpCallback<HttpData<VNotiesData>>(this) {

                    @Override
                    public void onSucceed(HttpData<VNotiesData> data) {
                        if(data.getData() != null){
                            VNotiesData model = data.getData();
                            if(isLoadMore){
                                srlNewFriends.finishLoadMore();
                                if(null != model) {
                                    List<VNotes> columns = model.content;
                                    if ((null != columns) && (columns.size() > 0)) {
                                        adapter.addData(columns);
                                    }else{
                                        ToastUtils.show("暂无更多数据");
                                    }
                                }

                            }else{
                                srlNewFriends.finishRefresh();
                                if(null != model) {
                                    List<VNotes> columns = model.content;
                                    if ((null != columns) && (columns.size() > 0)) {
                                        adapter.clearData();
                                        adapter.setList(columns);
                                    }else{
                                        ToastUtils.show("暂无更多数据");
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private void editeNewsColumn(int id){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userMessageId",id);
        EasyHttp.post(this)
                .api(new SysNotesUpdateApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        if(data.getData() != null){


                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

        Object item = adapter.getItem(position);
        if(item instanceof VNotes){
            if(StringUtils.isNotEmpty(((VNotes)item).detailUrl)){
                //editeNewsColumn(((VNotes)item).userMessageId);
                BrowserActivity.start(getContext(),((VNotes)item).detailUrl);
            }
        }

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getNewsColumn(10,page,true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
       page = 0;
        getNewsColumn(10,page,false);
    }
}
