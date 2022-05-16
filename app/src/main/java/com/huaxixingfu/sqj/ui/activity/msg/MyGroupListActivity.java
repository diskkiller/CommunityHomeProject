package com.huaxixingfu.sqj.ui.activity.msg;

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
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.GroupListBean;
import com.huaxixingfu.sqj.http.api.ApplyGroupListApi;
import com.huaxixingfu.sqj.http.api.CreatGroupListApi;
import com.huaxixingfu.sqj.http.api.JoinGroupListApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.CreatGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.GroupListAdapter;
import com.huaxixingfu.sqj.ui.adapter.TestAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.huaxixingfu.sqj.widget.SideIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyGroupListActivity extends AppActivity {

    private RecyclerView rv_apply,rv_creat,rv_join;
    private GroupListAdapter applyAdapter,creatAdapter,joinAdapter;
    private ArrayList<GroupListBean> applyData = new ArrayList<>();;
    private ArrayList<GroupListBean> creatData = new ArrayList<>();;
    private ArrayList<GroupListBean> joinData = new ArrayList<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_my_group;
    }

    @Override
    protected void initView() {
        rv_apply = findViewById(R.id.rv_apply);
        rv_creat = findViewById(R.id.rv_creat);
        rv_join = findViewById(R.id.rv_join);
        initRV();
    }

    public void initData() {
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
                TempMessageActivity.show(getContext(),targetUid,
                        targetUid+"",nickName,true);
            }
        });
        rv_creat.setAdapter(creatAdapter);


        joinAdapter = new GroupListAdapter(joinData,false);
        joinAdapter.setLayoutManager(llm3);
        joinAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //调用群聊界面

            }
        });
        rv_join.setAdapter(joinAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


}
