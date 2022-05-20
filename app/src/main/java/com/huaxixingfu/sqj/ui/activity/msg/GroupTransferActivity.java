package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.GroupMemberDetailApi;
import com.huaxixingfu.sqj.http.api.GroupTransferApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.msg.AddGroupMemberApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.GroupAddMemberHorAdapter;
import com.huaxixingfu.sqj.ui.adapter.GroupAddMemberVerAdapter;
import com.huaxixingfu.sqj.ui.adapter.GroupMemberListVerAdapter;
import com.huaxixingfu.sqj.utils.GsonUtil;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GroupTransferActivity extends AppActivity{

    private RecyclerView rv_member;
    private GroupMemberListVerAdapter groupMemberListVerAdapter;
    private ArrayList<GroupMemberBean> memberBeans = new ArrayList<>();

    private long targetUid;

    public static void start(BaseActivity activity, long targetUid, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, GroupTransferActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
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

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_transfer_group;
    }

    @Override
    protected void initView() {
        targetUid = getLong(IntentKey.TARGETUID);
        rv_member = findViewById(R.id.rv_member);
        setOnClickListener(R.id.tv_submit);
        initRV();
    }

    public void initData() {
        getGroupMemerDetail();

    }

    private void getGroupMemerDetail() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId",targetUid);
        map.put("niceName","");
        EasyHttp.post(this)
                .api(new GroupMemberDetailApi())
                .json(map)
                .request(new HttpCallback<HttpData<List<GroupMemberBean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<GroupMemberBean>> data) {
                        if(data.getData() != null) {

                            memberBeans = (ArrayList<GroupMemberBean>) data.getData();

                            groupMemberListVerAdapter.setList(memberBeans);

                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }



    private int memberId;
    private void initRV() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());


        rv_member.setLayoutManager(llm);
        groupMemberListVerAdapter = new GroupMemberListVerAdapter(memberBeans);
        groupMemberListVerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                for (int i = 0; i < memberBeans.size(); i++) {
                    if(i == position){
                        memberBeans.get(i).isSelect = true;
                    }else{
                        memberBeans.get(i).isSelect = false;
                    }
                }
                memberId = memberBeans.get(position).userId;
                groupMemberListVerAdapter.setList(memberBeans);
            }
        });
        rv_member.setAdapter(groupMemberListVerAdapter);
    }


    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_submit){
            transferGroup();
        }
    }

    private void transferGroup() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId",targetUid);
        map.put("chatUserId",memberId);
        EasyHttp.post(this)
                .api(new GroupTransferApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        toast("转让成功");
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


}
