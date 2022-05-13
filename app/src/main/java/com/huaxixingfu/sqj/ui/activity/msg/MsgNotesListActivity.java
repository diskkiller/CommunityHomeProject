package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.VNotes;
import com.huaxixingfu.sqj.bean.VNotiesData;
import com.huaxixingfu.sqj.databinding.SqjActivityGonggaoNotesListBinding;
import com.huaxixingfu.sqj.databinding.SqjActivitySystemNotesListBinding;
import com.huaxixingfu.sqj.http.api.MsgNotesListApi;
import com.huaxixingfu.sqj.http.api.MsgNotesListEditeApi;
import com.huaxixingfu.sqj.http.api.SysNotesListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.MsgNotesAdapter;
import com.huaxixingfu.sqj.ui.adapter.SystemNotesAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnStateChangedListener;

import java.util.HashMap;
import java.util.List;

/**
 * 作者：lblbh on 2022/4/30 20:38
 * 邮箱：lanbuhan@163.com
 * 功能：通知公告列表
 */

public class MsgNotesListActivity extends AppActivity implements OnRefreshLoadMoreListener,
        OnItemChildClickListener {
    MsgNotesAdapter adapter;
    RecyclerView recycler;

    int page = 1;

    private SmartRefreshLayout srlNewFriends;

    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_gonggao_notes_list;
    }

    @Override
    protected void initView() {
        srlNewFriends = findViewById(R.id.srl_new_friends);
        recycler = findViewById(R.id.recycler);

        adapter = new MsgNotesAdapter(R.layout.sqj_activity_gonggao_notes_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        srlNewFriends.setOnLoadMoreListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.addChildClickViewIds(R.id.item_find);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }

    public void initData(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        toast("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewsColumn(10,page,false);
    }

    private void getNewsColumn(int size, int page, boolean isLoadMore){
        HashMap<String, Object> map = new HashMap<>();
        map.put("size",size);
        map.put("page",page);
        map.put("id","");
        EasyHttp.post(this)
                .api(new MsgNotesListApi())
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
                                    if ((null != columns) || (columns.size() > 0)) {
                                        adapter.addData(columns);
                                    }else{
                                        ToastUtils.show("暂无更多数据");
                                    }
                                }

                            }else{
                                srlNewFriends.finishRefresh();
                                if(null != model) {
                                    List<VNotes> columns = model.content;
                                    if ((null != columns) || (columns.size() > 0)) {
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
                .api(new MsgNotesListEditeApi())
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getNewsColumn(10,page,true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getNewsColumn(10,page,false);
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter madapter, @NonNull View view, int position) {
        if(view.getId() == R.id.item_find){
            editeNewsColumn(adapter.getData().get(position).userMessageId);
            BrowserActivity.start(getContext(),adapter.getData().get(position).detailUrl);
        }
    }
}
