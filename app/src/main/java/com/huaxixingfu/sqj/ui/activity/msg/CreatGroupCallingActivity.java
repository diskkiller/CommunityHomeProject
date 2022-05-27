package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.base.BaseActivity;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.github.promeg.pinyinhelper.Pinyin;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.bean.GroupMemberBean;
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.GroupMemberDetailApi;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.msg.CreatGroutApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.ui.adapter.CallingSectionItemDecoration;
import com.huaxixingfu.sqj.ui.adapter.CreatCallingGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.CreatGroupAdapter;
import com.huaxixingfu.sqj.ui.adapter.GroupAddMemberHorAdapter;
import com.huaxixingfu.sqj.ui.adapter.SectionItemDecoration;
import com.huaxixingfu.sqj.utils.GsonUtil;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.utils.StringUtils;
import com.huaxixingfu.sqj.widget.SideIndexBar;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.TUICallingImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CreatGroupCallingActivity extends AppActivity implements SideIndexBar.OnIndexTouchedChangedListener{

    private RecyclerView rv_maillist;
    private SideIndexBar sib_maillist;
    private TextView tv_overlay;
    private CreatCallingGroupAdapter creatCallingGroupAdapter;

    private ArrayList<GroupMemberBean> memberBeans = new ArrayList<>();
    private long targetUid;

    private int callingType;

    public static final int TYPE_AUDIO_CALL       = 1;
    public static final int TYPE_VIDEO_CALL       = 2;
    public static final int TYPE_MULTI_AUDIO_CALL = 3;
    public static final int TYPE_MULTI_VIDEO_CALL = 4;

    private static final int MULTI_CALL_MAX_NUM        = 8; //C2C多人通话最大人数是9(需包含自己)

    public static void start(BaseActivity activity, int callingType,long targetUid, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, CreatGroupCallingActivity.class);
        intent.putExtra(IntentKey.TYPE, callingType);
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
        return R.layout.sqj_activity_creat_group_calling;
    }

    @Override
    protected void initView() {
        callingType = getInt(IntentKey.TYPE);
        targetUid = getLong(IntentKey.TARGETUID);


        rv_maillist = findViewById(R.id.rv_maillist);
        sib_maillist = findViewById(R.id.sib_maillist);
        tv_overlay = findViewById(R.id.tv_overlay);
        setOnClickListener(R.id.tv_creat_group);
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
                        if(data.getData() != null){

                            memberBeans = (ArrayList<GroupMemberBean>) data.getData();
                            GroupMemberBean owner = null;


                            if((null != memberBeans) && (memberBeans.size()>0)){

                                for (int i = 0,j= memberBeans.size(); i < j; i++) {
                                    GroupMemberBean item =  memberBeans.get(i);
                                    if(StringUtils.isNotEmpty(item.userNickName))
                                        item.first = String.valueOf(Pinyin.toPinyin(item.userNickName, "").charAt(0));
                                    if(item.userId == SPManager.instance(AppApplication.getInstances()).
                                            getModel(Constants.USERINFO, PersonDataBean.class).getUserId()){
                                        item.isAdd = true;
                                    }
                                }


                                Collections.sort(memberBeans, new Comparator<GroupMemberBean>() {
                                    @Override
                                    public int compare(GroupMemberBean o1, GroupMemberBean o2) {
                                        return o1.getSection().charAt(0) - o2.getSection().charAt(0);
                                    }
                                });

                                if (memberBeans.size() > 0) {
                                    rv_maillist.addItemDecoration(new CallingSectionItemDecoration(getContext(), memberBeans));
                                }

                                creatCallingGroupAdapter.setList(memberBeans);

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


        creatCallingGroupAdapter = new CreatCallingGroupAdapter(memberBeans);

        creatCallingGroupAdapter.setLayoutManager(llm);

        creatCallingGroupAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //FriendInfoActivity.start(CreatGroupActivity.this,contactsListBeans.get(position).chatUserId,contactsListBeans.get(position).chatFriendNiceName,null);
                GroupMemberBean dataBean = memberBeans.get(position);

                if(dataBean.isAdd) return;

                boolean isSelect = dataBean.isSelect;

                String chooseId = dataBean.userId + "";

                if (!isSelect) {

                    //groupAddMemberHorAdapter.addData(dataBean);

                    dataBean.isSelect = true;
                    if (StringUtils.isEmpty(memberId)) {
                        memberId = chooseId;
                    } else {
                        memberId = memberId + "," + chooseId;
                    }

                    creatCallingGroupAdapter.setList(memberBeans);
                } else {

                    //groupAddMemberHorAdapter.remove(dataBean);

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
                creatCallingGroupAdapter.setList(memberBeans);
                LogUtil.e("diskkiller","选中的ID :"+memberId);

            }
        });
        rv_maillist.setAdapter(creatCallingGroupAdapter);


    }

    @Override
    public void onIndexChanged(String index, int position) {
        creatCallingGroupAdapter.scrollToSection(index);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_creat_group){
            if(StringUtils.isEmpty(memberId))return;
            String[] member_arr = memberId.split(",");

            /*if (member_arr.length >= MULTI_CALL_MAX_NUM) {
                ToastUtils.showShort("添加失败,人数超出限制");
                return;
            }*/


            TUICalling.Type callType = (callingType == TYPE_VIDEO_CALL || callingType == TYPE_MULTI_VIDEO_CALL)
                    ? TUICalling.Type.VIDEO : TUICalling.Type.AUDIO;

            TUICallingImpl.sharedInstance(getApplication()).enableFloatWindow(true);
            TUICallingImpl.sharedInstance(getApplication()).call(member_arr, callType);

            finish();

        }
    }


}
