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
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.GroupMemberDetailApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.msg.AddGroupMemberApi;
import com.huaxixingfu.sqj.http.api.msg.CreatGroutApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.GroupAddMemberHorAdapter;
import com.huaxixingfu.sqj.ui.adapter.CreatGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.GroupAddMemberVerAdapter;
import com.huaxixingfu.sqj.utils.GsonUtil;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GroupAddMemberActivity extends AppActivity{

    private RecyclerView rv_maillist,rv_member;
    private GroupAddMemberVerAdapter groupAddMemberVerAdapter;
    private GroupAddMemberHorAdapter groupAddMemberHorAdapter;
    private ArrayList<MailListApi.Bean> contactsListBeans = new ArrayList<>();
    private ArrayList<MailListApi.Bean> addmemberList = new ArrayList<>();

    private ArrayList<GroupMemberBean> memberBeans = new ArrayList<>();

    private long targetUid;

    public static void start(BaseActivity activity, long targetUid, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, GroupAddMemberActivity.class);
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
        return R.layout.sqj_activity_group_addmember;
    }

    @Override
    protected void initView() {
        targetUid = getLong(IntentKey.TARGETUID);
        rv_maillist = findViewById(R.id.rv_maillist);
        rv_member = findViewById(R.id.rv_member);
        initRV();
    }

    @Override
    public void onRightClick(View view) {
        if(StringUtils.isEmpty(memberId))return;
        String[] member_arr = memberId.split(",");
        //数组转List
        //selMemberIdList =new ArrayList<>(Arrays.asList(member_arr));
        List<AddGroupMemberApi.Bean.Item> chatGroupCreateUserList = new ArrayList<>();
        for (String s : member_arr) {
            AddGroupMemberApi.Bean.Item member = new AddGroupMemberApi.Bean.Item();
            member.chatToUserId = Integer.parseInt(s);
            chatGroupCreateUserList.add(member);
        }
        AddGroupMemberApi.Bean modle = new AddGroupMemberApi.Bean();
        modle.chatGroupCreateUserList = chatGroupCreateUserList;
        modle.groupId = Integer.parseInt(targetUid+"");
        creatGroup(modle);
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
                                    if(StringUtils.isNotEmpty(item.userNickName))
                                        item.first = String.valueOf(Pinyin.toPinyin(item.userNickName, "").charAt(0));
                                }

                                contactsListBeans.clear();
                                contactsListBeans.addAll(datas);
                                Collections.sort(contactsListBeans, new Comparator<MailListApi.Bean>() {
                                    @Override
                                    public int compare(MailListApi.Bean o1, MailListApi.Bean o2) {
                                        return o1.getSection().charAt(0) - o2.getSection().charAt(0);
                                    }
                                });

                                //不设置数据，需要跟群成员对比是否群中包含该联系人
                                //groupAddMemberVerAdapter.setList(contactsListBeans);

                            }


                            getGroupMemerDetail();


                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
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

                            //联系人与群成员进行对比
                            for (MailListApi.Bean contactsListBean : contactsListBeans) {
                                for (GroupMemberBean memberBean : memberBeans) {
                                    if(contactsListBean.chatToUserId == memberBean.userId){
                                        contactsListBean.isAdd = true;//已经是群成员了
                                    }
                                }

                            }

                            groupAddMemberVerAdapter.setList(contactsListBeans);

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

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_maillist.setLayoutManager(llm);

        groupAddMemberVerAdapter = new GroupAddMemberVerAdapter(contactsListBeans);
        groupAddMemberVerAdapter.setLayoutManager(llm);
        groupAddMemberVerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //FriendInfoActivity.start(CreatGroupActivity.this,contactsListBeans.get(position).chatUserId,contactsListBeans.get(position).chatFriendNiceName,null);

                if(contactsListBeans.get(position).isAdd) return;

                MailListApi.Bean dataBean = contactsListBeans.get(position);
                boolean isSelect = dataBean.isSelect;
                String chooseId = dataBean.chatToUserId + "";

                if (!isSelect) {
                    groupAddMemberHorAdapter.addData(dataBean);

                    dataBean.isSelect = true;
                    if (StringUtils.isEmpty(memberId)) {
                        memberId = chooseId;
                    } else {
                        memberId = memberId + "," + chooseId;
                    }

                    groupAddMemberVerAdapter.setList(contactsListBeans);
                } else {
                    groupAddMemberHorAdapter.remove(dataBean);

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
                groupAddMemberVerAdapter.setList(contactsListBeans);
                LogUtil.e("diskkiller","选中的ID :"+memberId);

            }
        });
        rv_maillist.setAdapter(groupAddMemberVerAdapter);


        rv_member.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        groupAddMemberHorAdapter = new GroupAddMemberHorAdapter(addmemberList);
        groupAddMemberHorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });
        rv_member.setAdapter(groupAddMemberHorAdapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    private void creatGroup(AddGroupMemberApi.Bean modle) {
        String json = GsonUtil.gsonString(modle);
        EasyHttp.post(this)
                .api(new AddGroupMemberApi())
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
