package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.base.BaseAdapter;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.WrapRecyclerView;
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.RequestFriendCountApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.MaillListAdapter;
import com.huaxixingfu.sqj.ui.adapter.SectionItemDecoration;
import com.huaxixingfu.sqj.widget.SideIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MailListActivity extends AppActivity implements SideIndexBar.OnIndexTouchedChangedListener{

    private RecyclerView rv_maillist;
    private SideIndexBar sib_maillist;
    private TextView tv_overlay;
    private MaillListAdapter mailListAdapter;
    private TextView tv_new;
    private ArrayList<MailListApi.Bean> contactsListBeans = new ArrayList<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_maillist;
    }

    @Override
    protected void initView() {
        rv_maillist = findViewById(R.id.rv_maillist);
        sib_maillist = findViewById(R.id.sib_maillist);
        tv_overlay = findViewById(R.id.tv_overlay);
        initRV();
    }

    public void initData() {
        getMailList();
        getFriendCount();
    }

    private void getMailList(){
        EasyHttp.post(this)
                .api(new MailListApi())
                .json("")
                .request(new HttpCallback<HttpData<List<MailListApi.Bean>>>(this) {

                    @Override
                    public void onSucceed(HttpData<List<MailListApi.Bean>> data) {
                        if(data.getData() != null){
                            List<MailListApi.Bean> datas = data.getData();
                            if((null != datas) && (datas.size()>0)){
                                for (int i = 0,j= datas.size(); i < j; i++) {
                                    MailListApi.Bean item =  datas.get(i);
                                    item.first = String.valueOf(Pinyin.toPinyin(item.chatFriendNiceName, "").charAt(0));
                                }

                                contactsListBeans.clear();
                                contactsListBeans.addAll(datas);
                                Collections.sort(contactsListBeans, new Comparator<MailListApi.Bean>() {
                                    @Override
                                    public int compare(MailListApi.Bean o1, MailListApi.Bean o2) {
                                        return o1.getSection().charAt(0) - o2.getSection().charAt(0);
                                    }
                                });
                                if (contactsListBeans.size() > 0) {
                                    //增加header，增加对应的占位数据，用以处理ItemDecoration
                                    ArrayList<MailListApi.Bean> contactsListBeans1 = new ArrayList<>();
                                    MailListApi.Bean bean = new MailListApi.Bean();
                                    bean.first = "header";
                                    contactsListBeans1.add(bean);
                                    contactsListBeans1.addAll(contactsListBeans);
                                    rv_maillist.addItemDecoration(new SectionItemDecoration(getContext(), contactsListBeans1));
                                }

                                mailListAdapter.setList(contactsListBeans);

                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void getFriendCount(){
        EasyHttp.post(this)
                .api(new RequestFriendCountApi())
                .request(new HttpCallback<HttpData<RequestFriendCountApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<RequestFriendCountApi.Bean> data) {
                        if(data.getData() != null){
                            long count = data.getData().count;
                            if (tv_new == null) return;
                            if (count > 0) {
                                tv_new.setText(String.valueOf(count));
                                tv_new.setVisibility(View.VISIBLE);
                            } else {
                                tv_new.setVisibility(View.GONE);
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

        sib_maillist.setOverlayTextView(tv_overlay).setOnIndexChangedListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_maillist.setLayoutManager(llm);


        mailListAdapter = new MaillListAdapter(contactsListBeans);

        View titleView = View.inflate(getContext(), R.layout.sqj_item_maillist_title, null);
        RelativeLayout rl_new_friends = titleView.findViewById(R.id.rl_new_friends);
        RelativeLayout rl_my_groups = titleView.findViewById(R.id.rl_my_groups);
        RelativeLayout rl_my_shegong = titleView.findViewById(R.id.rl_my_shegong);
        rl_new_friends.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NewFriendsActivity.class));
        });
        rl_my_groups.setOnClickListener(v -> {

        });
        rl_my_shegong.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ResidentListActivity.class));
        });
        tv_new = titleView.findViewById(R.id.tv_new);

        mailListAdapter.setLayoutManager(llm);
        mailListAdapter.addHeaderView(titleView);

        mailListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                FriendInfoActivity.start(MailListActivity.this,contactsListBeans.get(position).chatUserId,contactsListBeans.get(position).chatFriendNiceName,null);

            }
        });
        rv_maillist.setAdapter(mailListAdapter);
    }

    @Override
    public void onIndexChanged(String index, int position) {
        mailListAdapter.scrollToSection(index);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }


}
