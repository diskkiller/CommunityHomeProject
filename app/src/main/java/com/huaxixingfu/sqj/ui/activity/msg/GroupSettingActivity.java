package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.base.BaseDialog;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.diskkiller.widget.layout.SettingBar;
import com.hjq.toast.ToastUtils;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.bean.GroupDetailBean;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.bean.VCode;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.DelGroupMemberApi;
import com.huaxixingfu.sqj.http.api.EditGroupFriendNikeNameApi;
import com.huaxixingfu.sqj.http.api.EditGroupNikeNameApi;
import com.huaxixingfu.sqj.http.api.EditNikeNameApi;
import com.huaxixingfu.sqj.http.api.GroupDetailApi;
import com.huaxixingfu.sqj.http.api.GroupMemberDetailApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.activity.me.report.ReportContentListActivity;
import com.huaxixingfu.sqj.ui.activity.me.report.ReportSubmitActivity;
import com.huaxixingfu.sqj.ui.adapter.GroupMemberAdapter;
import com.huaxixingfu.sqj.ui.dialog.InputDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupSettingActivity extends AppActivity {

    private RecyclerView gv_member;
    private GroupMemberAdapter groupMemberAdapter;
    private ArrayList<String> testData = new ArrayList<>();
    private ArrayList<String> testLessData = new ArrayList<>();
    private ArrayList<String> testMoreData = new ArrayList<>();
    private ArrayList<GroupMemberBean> memberBeans = new ArrayList<>();
    private ArrayList<GroupMemberBean> lessMemberBeans = new ArrayList<>();
    private TextView tx_more;
    private long targetUid;
    private boolean isOwner;
    private SettingBar sb_group_name,sb_group_my_nickname,sb_group_transfer;

    public static void start(BaseActivity activity, long targetUid, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, GroupSettingActivity.class);
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
        return R.layout.sqj_activity_group_setting;
    }

    @Override
    protected void initView() {
        targetUid = getLong(IntentKey.TARGETUID);
        gv_member = findViewById(R.id.member_gridview);
        tx_more = findViewById(R.id.tx_more);
        sb_group_name = findViewById(R.id.sb_group_name);
        sb_group_my_nickname = findViewById(R.id.sb_group_my_nickname);
        sb_group_transfer = findViewById(R.id.sb_group_transfer);
        setOnClickListener(R.id.tx_more,R.id.sb_group_name,R.id.sb_group_notice,R.id.sb_group_transfer,R.id.sb_group_my_nickname,R.id.sb_chat_report);
        initRV();
    }

    public void initData() {
        //analogData();//模拟请求数据
        //newReGetData();//模拟对请求回来的数据进行重组
    }

    @Override
    protected void onStart() {
        super.onStart();
        getGroupDetail();
    }

    private void getGroupDetail() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId",targetUid);
        EasyHttp.post(this)
                .api(new GroupDetailApi())
                .json(map)
                .request(new HttpCallback<HttpData<GroupDetailBean>>(this) {

                    @Override
                    public void onSucceed(HttpData<GroupDetailBean> data) {
                        if(data.getData() != null){

                            GroupDetailBean groupDetailBean = data.getData();

                            isOwner = groupDetailBean.getIsChatGroupUserId();
                            if(!isOwner){
                                sb_group_transfer.setVisibility(View.GONE);
                            }
                            getGroupMemerDetail();
                        /*
                            //组装自己
                            GroupMemberBean self = new GroupMemberBean();
                            self.nickname = groupDetailBean.getChatUserNiceName();
                            memberBeans.add(self);

                            //组装其他群成员
                            String[] memberNicknames = groupDetailBean.getChatGroupNiceName().split("、");
                            for (String memberNickname : memberNicknames) {
                                if(StringUtils.isNotEmpty(memberNickname)){
                                    GroupMemberBean member = new GroupMemberBean();
                                    member.nickname = memberNickname;
                                    memberBeans.add(member);
                                }
                            }

                            //组装全部群成员头像
                            List<String> memberAvatarUrls = groupDetailBean.getUserAvatarUrl();
                            if(memberAvatarUrls != null && memberAvatarUrls.size()>0){
                                for (int i = 0; i < memberAvatarUrls.size(); i++) {
                                    memberBeans.get(i).userAvatarUrl = memberAvatarUrls.get(i);
                                }
                            }

                            //将群主提取到列表头部，需服务器返回群成员ID进行对比，待做,将群主插入到列表头（如果数据里群主不在头部）
                            for(int i = 0; i<memberBeans.size() ; i++){
                                if(null != mList.get(i).getIs_owner() && mList.get(i).getIs_owner() == 1){
                                    owner = mList.get(i);
                                    mList.remove(i);
                                    break;
                                }
                            }
                            mList.add(0,owner);


                            if(memberBeans.size() > 18){
                                tx_more.setVisibility(View.VISIBLE);
                                lessMemberBeans.clear();
                                for (int i = 0; i < 18; i++) {
                                    lessMemberBeans.add(memberBeans.get(i));
                                }
                                GroupMemberBean add = new GroupMemberBean();
                                add.nickname = "+";
                                GroupMemberBean minus = new GroupMemberBean();
                                minus.nickname = "-";
                                lessMemberBeans.add(add);
                                lessMemberBeans.add(minus);
                                groupMemberAdapter.setList(lessMemberBeans);
                            }else{
                                tx_more.setVisibility(View.GONE);
                                GroupMemberBean add = new GroupMemberBean();
                                add.nickname = "+";
                                GroupMemberBean minus = new GroupMemberBean();
                                minus.nickname = "-";
                                memberBeans.add(add);
                                memberBeans.add(minus);
                                groupMemberAdapter.setList(memberBeans);
                            }
                            */
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
                        if(data.getData() != null){

                            memberBeans = (ArrayList<GroupMemberBean>) data.getData();
                            GroupMemberBean owner = null;

                            //需服务器返回群成员ID进行对比，将群主插入到列表头（如果数据里群主不在头部）
                            for(int i = 0; i<memberBeans.size() ; i++){
                                if(memberBeans.get(i).isChatGroupUserId){
                                    owner = memberBeans.get(i);
                                    memberBeans.remove(i);
                                    break;
                                }
                            }
                            if(owner != null)
                                memberBeans.add(0,owner);

                            /**
                             * 组装数据+、-
                             * @return
                             */
                            if(memberBeans.size() > 18){
                                tx_more.setVisibility(View.VISIBLE);
                                lessMemberBeans.clear();
                                for (int i = 0; i < 18; i++) {
                                    lessMemberBeans.add(memberBeans.get(i));
                                }
                                GroupMemberBean add = new GroupMemberBean();
                                add.nickname = "+";
                                lessMemberBeans.add(add);
                                if(isOwner){
                                    GroupMemberBean minus = new GroupMemberBean();
                                    minus.nickname = "-";
                                    lessMemberBeans.add(minus);
                                }
                                groupMemberAdapter.setList(lessMemberBeans);
                            }else{
                                tx_more.setVisibility(View.GONE);
                                GroupMemberBean add = new GroupMemberBean();
                                add.nickname = "+";
                                memberBeans.add(add);
                                if(isOwner){
                                    GroupMemberBean minus = new GroupMemberBean();
                                    minus.nickname = "-";
                                    memberBeans.add(minus);
                                }
                                groupMemberAdapter.setList(memberBeans);
                            }

                        }


                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }




    /**
     * 模拟数据
     */
    private void analogData() {
        for (int i = 0; i <19; i++) {
            String str = "昵称" + i;
            testData.add(str);
        }

    }

    private void initRV() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gv_member.setLayoutManager(gridLayoutManager);

        groupMemberAdapter = new GroupMemberAdapter();

        groupMemberAdapter.addChildClickViewIds(R.id.badge_delete);

        groupMemberAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if(view.getId() == R.id.badge_delete){
                    delGroupMemer(Integer.parseInt(targetUid+""),groupMemberAdapter.getItem(position).userId,position);
                }
            }
        });

        groupMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //toast(groupMemberAdapter.getItem(position).nickname);
                if(groupMemberAdapter.getItem(position).nickname.equals("+")){
                    GroupAddMemberActivity.start(GroupSettingActivity.this,targetUid,null);
                }else if(groupMemberAdapter.getItem(position).nickname.equals("-")){
                    groupMemberAdapter.setMinus(!groupMemberAdapter.isMinus());
                    groupMemberAdapter.notifyDataSetChanged();
                }else{
                    if(groupMemberAdapter.getItem(position).isFriend){
                        FriendInfoActivity.start(GroupSettingActivity.this,
                                groupMemberAdapter.getItem(position).userId, null);
                    }else{
                        AddFriendApplyActivity.start(GroupSettingActivity.this,
                                groupMemberAdapter.getItem(position).userId,
                                groupMemberAdapter.getItem(position).nickname,
                                groupMemberAdapter.getItem(position).residentAvatarUrl,null);
                    }

                }
            }
        });
        gv_member.setAdapter(groupMemberAdapter);

    }

    private void delGroupMemer(int targetUid, int chatToUserId, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId",targetUid);
        map.put("chatToUserId",chatToUserId);
        EasyHttp.post(this)
                .api(new DelGroupMemberApi())
                .json(map)
                .request(new HttpCallback<HttpData>(this) {

                    @Override
                    public void onSucceed(HttpData data) {
                        groupMemberAdapter.getData().remove(position);
                        groupMemberAdapter.notifyItemRemoved(position);
                        groupMemberAdapter.notifyItemRangeChanged(position, groupMemberAdapter.getItemCount());
                        toast("删除成功");
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tx_more){
            GroupMemberBean add = new GroupMemberBean();
            add.nickname = "+";
            memberBeans.add(add);
            if(isOwner){
                GroupMemberBean minus = new GroupMemberBean();
                minus.nickname = "-";
                memberBeans.add(minus);
            }
            groupMemberAdapter.setList(memberBeans);
            tx_more.setVisibility(View.GONE);
        }else if(id == R.id.sb_group_name){
            if(!isOwner){
                toast("不可修改");
                return;
            }
            new InputDialog.Builder(getContext())
                    .setTitle("群聊名称设置")
                    .setContent("")
                    .setHint("请输入群聊名称")
                    .setCancelable(false)
                    .setListener(new InputDialog.OnListener() {

                        @Override
                        public void onConfirm(BaseDialog dialog, String content) {
                            editGroupNikeName(content);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {

                        }
                    })
                    .show();

        }else if(id == R.id.sb_group_notice){
            GroupNotesListActivity.start(GroupSettingActivity.this,targetUid,isOwner,null);
        }else if(id == R.id.sb_group_transfer){
            GroupTransferActivity.start(GroupSettingActivity.this,targetUid,null);
        }else if(id == R.id.sb_group_my_nickname){
            new InputDialog.Builder(getContext())
                    .setTitle("昵称设置")
                    .setContent("")
                    .setHint("请输入昵称")
                    .setCancelable(false)
                    .setListener(new InputDialog.OnListener() {

                        @Override
                        public void onConfirm(BaseDialog dialog, String content) {
                            editGroupFriendNikeName(content);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {

                        }
                    })
                    .show();
        }else if(id == R.id.sb_chat_report){
            ReportSubmitActivity.start(GroupSettingActivity.this,ReportContentListActivity.GROUPSETTING,targetUid);

        }
    }

    private void editGroupNikeName(String name){
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId", targetUid);
        map.put("chatGroupNiceName", name);
        EasyHttp.post(this)
                .api(new EditGroupNikeNameApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        sb_group_name.setRightText(name);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }
    private void editGroupFriendNikeName(String name){
        HashMap<String, Object> map = new HashMap<>();
        map.put("groupId", targetUid);
        map.put("chatGroupNiceName", name);
        EasyHttp.post(this)
                .api(new EditGroupFriendNikeNameApi())
                .json(map)
                .request(new HttpCallback<HttpData<VCode>>(this) {

                    @Override
                    public void onSucceed(HttpData<VCode> data) {
                        sb_group_my_nickname.setRightText(name);
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }



}
