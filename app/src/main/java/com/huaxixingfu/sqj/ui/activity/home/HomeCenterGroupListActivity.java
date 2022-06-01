package com.huaxixingfu.sqj.ui.activity.home;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.diskkiller.base.BaseActivity;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.HomeCenterBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.ui.activity.msg.TempMessageActivity;
import com.huaxixingfu.sqj.ui.adapter.HomeCenterGroupListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeCenterGroupListActivity extends AppActivity {

    private RecyclerView rv_group;
    private HomeCenterGroupListAdapter homeCenterGroupListAdapter;
    private ArrayList<HomeCenterBean.ChatRoomVOList> groupData = new ArrayList<>();

    public static void start(BaseActivity activity, List<HomeCenterBean.ChatRoomVOList> datas, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, HomeCenterGroupListActivity.class);
        intent.putExtra(IntentKey.OBJECT_LIST, (Serializable) datas);
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
        return R.layout.sqj_activity_home_center_group;
    }

    @Override
    protected void initView() {
        rv_group = findViewById(R.id.rv_group);
        groupData = getSerializable(IntentKey.OBJECT_LIST);
        initRV();
    }

    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void initRV() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        rv_group.setLayoutManager(llm);

        homeCenterGroupListAdapter = new HomeCenterGroupListAdapter(groupData,true);
        homeCenterGroupListAdapter.setLayoutManager(llm);
        homeCenterGroupListAdapter.addChildClickViewIds(R.id.tv_group_status);
        homeCenterGroupListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if(view.getId() == R.id.tv_group_status){

                    long targetUid = groupData.get(position).chatGroupId;
                    String nickName = groupData.get(position).chatRoomName;

                    TempMessageActivity.show(HomeCenterGroupListActivity.this,targetUid,
                            targetUid+"",nickName,true,null);

                }
            }
        });
        rv_group.setAdapter(homeCenterGroupListAdapter);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }


}
