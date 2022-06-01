package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.GroupListBean;
import com.huaxixingfu.sqj.http.api.ApplyGroupApi;
import com.huaxixingfu.sqj.http.api.ApplyGroupListApi;
import com.huaxixingfu.sqj.http.api.CreatGroupListApi;
import com.huaxixingfu.sqj.http.api.JoinGroupListApi;
import com.huaxixingfu.sqj.http.api.NeverGroupApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.GroupListAdapter;
import com.scwang.smart.refresh.layout.listener.OnStateChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGroupListActivity extends AppActivity {

    private RecyclerView rv_apply,rv_creat,rv_join;
    private GroupListAdapter applyAdapter,creatAdapter,joinAdapter;
    private ArrayList<GroupListBean> applyData = new ArrayList<>();
    private ArrayList<GroupListBean> creatData = new ArrayList<>();
    private ArrayList<GroupListBean> joinData = new ArrayList<>();

    private TextView tv_apply_title,tv_creat_title,tv_join_title;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_my_group;
    }

    @Override
    protected void initView() {
        rv_apply = findViewById(R.id.rv_apply);
        rv_creat = findViewById(R.id.rv_creat);
        rv_join = findViewById(R.id.rv_join);
        tv_apply_title = findViewById(R.id.tv_apply_title);
        tv_creat_title = findViewById(R.id.tv_creat_title);
        tv_join_title = findViewById(R.id.tv_join_title);

        setOnClickListener(R.id.ll_creat_group);
        initRV();
    }

    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getApplyList();
        getCreatList();
        getJoinList();
    }

    private void getApplyList(){
        EasyHttp.post(this)
                .api(new ApplyGroupListApi())
                .json("")
                .request(new HttpCallback<HttpData<List<GroupListBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<GroupListBean>> data) {
                        if(data.getData() != null){
                            List<GroupListBean> datas = data.getData();
                            if((null != datas) && (datas.size()>0)){
                                applyData.clear();
                                applyData.addAll(datas);
                                applyAdapter.setList(applyData);

                            }else{
                                tv_apply_title.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getCreatList(){
        EasyHttp.post(this)
                .api(new CreatGroupListApi())
                .json("")
                .request(new HttpCallback<HttpData<List<GroupListBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<GroupListBean>> data) {
                        if(data.getData() != null){
                            List<GroupListBean> datas = data.getData();
                            if((null != datas) && (datas.size()>0)){
                                creatData.clear();
                                creatData.addAll(datas);
                                creatAdapter.setList(creatData);

                            }else{
                                tv_creat_title.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getJoinList(){
        EasyHttp.post(this)
                .api(new JoinGroupListApi())
                .json("")
                .request(new HttpCallback<HttpData<List<GroupListBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<GroupListBean>> data) {
                        if(data.getData() != null){
                            List<GroupListBean> datas = data.getData();
                            if((null != datas) && (datas.size()>0)){
                                joinData.clear();
                                joinData.addAll(datas);
                                joinAdapter.setList(joinData);

                            }else{
                                tv_join_title.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    private void groupApply(int chatGroupApplyId, int position){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatGroupApplyId",chatGroupApplyId);
        EasyHttp.post(this)
                .api(new ApplyGroupApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        applyData.remove(position);
                        applyAdapter.notifyItemRemoved(position);
                        applyAdapter.notifyItemRangeChanged(position, applyAdapter.getItemCount());
                        getJoinList();
                        getApplyList();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void groupNever(int chatGroupApplyId, int position){
        HashMap<String, Object> map = new HashMap<>();
        map.put("chatGroupApplyId",chatGroupApplyId);
        EasyHttp.post(this)
                .api(new NeverGroupApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        applyData.remove(position);
                        applyAdapter.notifyItemRemoved(position);
                        applyAdapter.notifyItemRangeChanged(position, applyAdapter.getItemCount());
                        getApplyList();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    private void initRV() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
        rv_apply.setLayoutManager(llm);
        rv_creat.setLayoutManager(llm2);
        rv_join.setLayoutManager(llm3);


        applyAdapter = new GroupListAdapter(applyData,true);
        applyAdapter.setLayoutManager(llm);
        applyAdapter.addChildClickViewIds(R.id.tv_agree,R.id.tv_never);
        applyAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if(view.getId() == R.id.tv_agree){
                    /**
                     * 去请求服务器
                     *
                     * 服务器返回成功调用：
                     *                 applyData.remove(position);
                     *                 applyAdapter.notifyItemRemoved(position);
                     *                 applyAdapter.notifyItemRangeChanged(position, applyAdapter.getItemCount());
                     */

                    groupApply(applyData.get(position).chatGroupApplyId,position);

                }else if(view.getId() == R.id.tv_never){
                   groupNever(applyData.get(position).chatGroupApplyId,position);
                }
            }
        });
        rv_apply.setAdapter(applyAdapter);


        creatAdapter = new GroupListAdapter(applyData,false);
        creatAdapter.setLayoutManager(llm2);
        creatAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //调用群聊界面
                int targetUid = creatData.get(position).groupId;
                String nickName = creatData.get(position).chatGroupNiceName;
                TempMessageActivity.show(MyGroupListActivity.this,targetUid,
                        targetUid+"",nickName,true,null);
            }
        });
        rv_creat.setAdapter(creatAdapter);


        joinAdapter = new GroupListAdapter(joinData,false);
        joinAdapter.setLayoutManager(llm3);
        joinAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //调用群聊界面
                int targetUid = joinData.get(position).groupId;
                String nickName = joinData.get(position).chatGroupNiceName;
                TempMessageActivity.show(MyGroupListActivity.this,targetUid,
                        targetUid+"",nickName,true,null);
            }
        });
        rv_join.setAdapter(joinAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ll_creat_group){
            startActivity(new Intent(getContext(), CreatGroupActivity.class));
        }
    }


}
