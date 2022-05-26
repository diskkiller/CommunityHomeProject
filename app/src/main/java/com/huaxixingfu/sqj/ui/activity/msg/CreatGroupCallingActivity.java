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
import com.huaxixingfu.sqj.bean.PersonDataBean;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.http.api.MailListApi;
import com.huaxixingfu.sqj.http.api.msg.CreatGroutApi;
import com.huaxixingfu.sqj.http.model.HttpData;
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
import java.util.List;

public class CreatGroupCallingActivity extends AppActivity implements SideIndexBar.OnIndexTouchedChangedListener{

    private RecyclerView rv_maillist,rv_member;
    private SideIndexBar sib_maillist;
    private TextView tv_overlay;
    private CreatGroupAdapter creatGroupAdapter;
    private GroupAddMemberHorAdapter groupAddMemberHorAdapter;
    private ArrayList<MailListApi.Bean> contactsListBeans = new ArrayList<>();;
    private ArrayList<MailListApi.Bean> addmemberList = new ArrayList<>();

    private int callingType;

    public static final int TYPE_AUDIO_CALL       = 1;
    public static final int TYPE_VIDEO_CALL       = 2;
    public static final int TYPE_MULTI_AUDIO_CALL = 3;
    public static final int TYPE_MULTI_VIDEO_CALL = 4;

    private static final int MULTI_CALL_MAX_NUM        = 8; //C2C多人通话最大人数是9(需包含自己)

    public static void start(BaseActivity activity, int callingType, OnFinishResultListener listener) {
        Intent intent = new Intent(activity, CreatGroupCallingActivity.class);
        intent.putExtra(IntentKey.TYPE, callingType);
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



        rv_maillist = findViewById(R.id.rv_maillist);
        rv_member = findViewById(R.id.rv_member);
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

                                if (contactsListBeans.size() > 0) {
                                    rv_maillist.addItemDecoration(new SectionItemDecoration(getContext(), contactsListBeans));
                                }

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

                    //groupAddMemberHorAdapter.addData(dataBean);

                    dataBean.isSelect = true;
                    if (StringUtils.isEmpty(memberId)) {
                        memberId = chooseId;
                    } else {
                        memberId = memberId + "," + chooseId;
                    }

                    creatGroupAdapter.setList(contactsListBeans);
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
                creatGroupAdapter.setList(contactsListBeans);
                LogUtil.e("diskkiller","选中的ID :"+memberId);

            }
        });
        rv_maillist.setAdapter(creatGroupAdapter);


        /*rv_member.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        groupAddMemberHorAdapter = new GroupAddMemberHorAdapter(addmemberList);
        groupAddMemberHorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });
        rv_member.setAdapter(groupAddMemberHorAdapter);*/

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
