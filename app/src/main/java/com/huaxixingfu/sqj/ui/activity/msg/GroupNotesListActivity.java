package com.huaxixingfu.sqj.ui.activity.msg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.VGroupNotes;
import com.huaxixingfu.sqj.bean.VGroupNotiesData;
import com.huaxixingfu.sqj.bean.VNotes;
import com.huaxixingfu.sqj.bean.VNotiesData;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.GroupNotesListApi;
import com.huaxixingfu.sqj.http.api.MsgNotesListApi;
import com.huaxixingfu.sqj.http.api.MsgNotesListEditeApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.other.BrowserActivity;
import com.huaxixingfu.sqj.ui.adapter.GroupNotesAdapter;
import com.huaxixingfu.sqj.ui.adapter.MsgNotesAdapter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

/**
 * 功能：群公告列表
 */

public class GroupNotesListActivity extends AppActivity implements OnRefreshLoadMoreListener,
        OnItemChildClickListener {
    GroupNotesAdapter adapter;
    RecyclerView recycler;

    int page = 0;

    private SmartRefreshLayout srlNewFriends;

    private long groupId;
    private boolean isOwner;

    private TitleBar titleBar;


    public static void start(BaseActivity activity, long targetUid,boolean isOwner, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, GroupNotesListActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
        intent.putExtra(IntentKey.KEY_IS_GROUP_OWNER, isOwner);
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

    @SuppressLint("NewApi")
    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_gonggao_notes_group_list;
    }

    @Override
    protected void initView() {

        titleBar = findViewById(R.id.tb_title);

        groupId = getLong(IntentKey.TARGETUID);
        isOwner = getBoolean(IntentKey.KEY_IS_GROUP_OWNER);

        if(isOwner){
            titleBar.setRightTitle("新增");
            titleBar.setRightTitleColor(Color.RED);
        }

        srlNewFriends = findViewById(R.id.srl_new_friends);
        recycler = findViewById(R.id.recycler);

        adapter = new GroupNotesAdapter(R.layout.sqj_activity_gonggao_group_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        srlNewFriends.setOnLoadMoreListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.addChildClickViewIds(R.id.item_tips,R.id.item_del);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onRightClick(View view) {
        InputContentActivity.start(GroupNotesListActivity.this,groupId,null);
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
        getNotesColumn(10,page,Integer.parseInt(groupId+""),false);
    }

    private void getNotesColumn(int size, int page,int groupId, boolean isLoadMore){
        HashMap<String, Object> map = new HashMap<>();
        map.put("size",size);
        map.put("page",page);
        map.put("groupId",groupId);
        EasyHttp.post(this)
                .api(new GroupNotesListApi())
                .json(map)
                .request(new HttpCallback<HttpData<VGroupNotiesData>>(this) {

                    @Override
                    public void onSucceed(HttpData<VGroupNotiesData> data) {
                        if(data.getData() != null){
                            VGroupNotiesData model = data.getData();
                            if(isLoadMore){
                                srlNewFriends.finishLoadMore();
                                if(null != model) {
                                    List<VGroupNotes> columns = model.content;
                                    if ((null != columns) || (columns.size() > 0)) {
                                        adapter.addData(columns);
                                    }else{
                                        ToastUtils.show("暂无更多数据");
                                    }
                                }

                            }else{
                                srlNewFriends.finishRefresh();
                                if(null != model) {
                                    List<VGroupNotes> columns = model.content;
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
        getNotesColumn(10,page,Integer.parseInt(groupId+""),true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 0;
        getNotesColumn(10,page,Integer.parseInt(groupId+""),false);
    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter madapter, @NonNull View view, int position) {
        if(view.getId() == R.id.item_find){
            //editeNewsColumn(adapter.getData().get(position).userMessageId);
            //BrowserActivity.start(getContext(),adapter.getData().get(position).detailUrl);
        }
    }
}
