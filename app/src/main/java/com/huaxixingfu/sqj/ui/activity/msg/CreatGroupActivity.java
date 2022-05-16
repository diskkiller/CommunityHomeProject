package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.RequestFriendCountApi;
import com.huaxixingfu.sqj.http.api.msg.CreatGroutApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.CreatGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.MaillListAdapter;
import com.huaxixingfu.sqj.ui.adapter.SectionItemDecoration;
import com.huaxixingfu.sqj.utils.GsonUtil;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.huaxixingfu.sqj.widget.SideIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreatGroupActivity extends AppActivity implements SideIndexBar.OnIndexTouchedChangedListener{

    private RecyclerView rv_maillist;
    private SideIndexBar sib_maillist;
    private TextView tv_overlay;
    private CreatGroupAdapter creatGroupAdapter;
    private ArrayList<MailListApi.Bean> contactsListBeans = new ArrayList<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_activity_creat_group;
    }

    @Override
    protected void initView() {
        rv_maillist = findViewById(R.id.rv_maillist);
        sib_maillist = findViewById(R.id.sib_maillist);
        tv_overlay = findViewById(R.id.tv_overlay);
        setOnClickListener(R.id.tv_creat_group);
        initRV();
    }

    public void initData() {
        getMailList();
        //getFriendCount();
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

                                creatGroupAdapter.setList(contactsListBeans);

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
    private void initRV() {

        sib_maillist.setOverlayTextView(tv_overlay).setOnIndexChangedListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_maillist.setLayoutManager(llm);


        creatGroupAdapter = new CreatGroupAdapter(contactsListBeans);

        creatGroupAdapter.setLayoutManager(llm);

        creatGroupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //FriendInfoActivity.start(CreatGroupActivity.this,contactsListBeans.get(position).chatUserId,contactsListBeans.get(position).chatFriendNiceName,null);
                MailListApi.Bean dataBean = contactsListBeans.get(position);
                boolean isSelect = dataBean.isSelect;

                String chooseId = dataBean.chatToUserId + "";

                if (!isSelect) {
                    dataBean.isSelect = true;
                    if (StringUtils.isEmpty(memberId)) {
                        memberId = chooseId;
                    } else {
                        memberId = memberId + "," + chooseId;
                    }

                    creatGroupAdapter.setList(contactsListBeans);
                } else {
                    dataBean.isSelect = false;
                    String again = "";

                    String[] split = memberId.split(",");
                    for (int i = 0; i < split.length; i++) {
                        if (!split[i].equals(chooseId)) {
                            if (StringUtils.isEmpty(again)) {
                                again = split[i];
                            } else {
                                again = again + "," + split[i];
                            }
                        }
                    }
                    memberId = again;
                }
                creatGroupAdapter.setList(contactsListBeans);
                LogUtil.e("diskkiller","选中的ID :"+memberId);

            }
        });
        rv_maillist.setAdapter(creatGroupAdapter);
    }

    @Override
    public void onIndexChanged(String index, int position) {
        creatGroupAdapter.scrollToSection(index);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_creat_group){
            if(StringUtils.isEmpty(memberId))return;
            String[] member_arr = memberId.split(",");
            //数组转List
            //selMemberIdList =new ArrayList<>(Arrays.asList(member_arr));
            List<CreatGroutApi.Bean.Item> chatGroupCreateUserList = new ArrayList<>();
            for (String s : member_arr) {
                CreatGroutApi.Bean.Item member = new CreatGroutApi.Bean.Item();
                member.chatToUserId = Integer.parseInt(s);
                chatGroupCreateUserList.add(member);
            }
            CreatGroutApi.Bean modle = new CreatGroutApi.Bean();
            modle.chatGroupCreateUserList = chatGroupCreateUserList;
            creatGroup(modle);
        }
    }

    private void creatGroup(CreatGroutApi.Bean modle) {
        String json = GsonUtil.gsonString(modle);
        EasyHttp.post(this)
                .api(new CreatGroutApi())
                .json(json)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        toast("添加成功");
                        finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


}
