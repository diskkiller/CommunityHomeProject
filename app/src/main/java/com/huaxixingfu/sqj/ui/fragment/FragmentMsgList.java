package com.huaxixingfu.sqj.ui.fragment;

import static com.shehuan.niv.Utils.dp2px;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.diskkiller.http.EasyHttp;
import com.diskkiller.http.listener.HttpCallback;
import com.google.gson.Gson;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.bean.UserData;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.http.api.FriendApplyCountApi;
import com.huaxixingfu.sqj.http.model.HttpData;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.huaxixingfu.sqj.push.bean.ResponseConersationList;
import com.huaxixingfu.sqj.push.bean.ResponseMessage;
import com.huaxixingfu.sqj.push.manager.MessageListener;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.ui.activity.login.LoginActivity;
import com.huaxixingfu.sqj.ui.activity.msg.CreatGroupActivity;
import com.huaxixingfu.sqj.ui.activity.msg.MailListActivity;
import com.huaxixingfu.sqj.ui.activity.msg.MsgMissionListActivity;
import com.huaxixingfu.sqj.ui.activity.msg.MsgNotesListActivity;
import com.huaxixingfu.sqj.ui.activity.msg.SearchFriendActivity;
import com.huaxixingfu.sqj.ui.activity.msg.TempMessageActivity;
import com.huaxixingfu.sqj.ui.adapter.MsgListAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okio.ByteString;

public class FragmentMsgList extends TitleBarFragment<AppActivity> implements OnRefreshLoadMoreListener,
        OnItemClickListener {

    private View view;
    private RecyclerView rv_msg_list;
    private RelativeLayout rl_extend;
    private TextView tv_right;
    private TextView tv_login;
    private int page = 0;
    private MsgListAdapter msgListAdapter;
    private ArrayList<Conersation> msgList = new ArrayList<>();
    private MessageListener.MessageObserver messageObserver;
    private ActivityResultLauncher<Intent> launcher;
    private TextView tv_right_red,tv_gonggao_new,tv_mission_new,tv_gonggao_content,tv_renwu_content;

    public static FragmentMsgList newInstance() {
        return new FragmentMsgList();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // ????????????????????????
        return !super.isStatusBarEnabled();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sqj_fragment_msglist;
    }

    @Override
    protected void initView() {
        rv_msg_list = findViewById(R.id.rv_msg_list);
        tv_login = findViewById(R.id.tv_login);
        tv_right_red = findViewById(R.id.tv_right_red);

        msgListAdapter = new MsgListAdapter(R.layout.sqj_item_msg_list);
        msgListAdapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        rv_msg_list.setLayoutManager(linearLayoutManager);
        rv_msg_list.setAdapter(msgListAdapter);

        View headerView = View.inflate(getContext(), R.layout.sqj_item_msglist_header, null);
        LinearLayout ll_system = headerView.findViewById(R.id.ll_gonggao);
        ll_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_gonggao_new.setVisibility(View.GONE);
                tv_gonggao_content.setText("????????????????????????");
                startActivity(new Intent(getActivity(), MsgNotesListActivity.class));
            }
        });

        tv_gonggao_new = headerView.findViewById(R.id.tv_gonggao_new);
        tv_mission_new = headerView.findViewById(R.id.tv_mission_new);

        tv_gonggao_content = headerView.findViewById(R.id.tv_gonggao_content);
        tv_renwu_content = headerView.findViewById(R.id.tv_renwu_content);

        LinearLayout ll_msg_notes = headerView.findViewById(R.id.ll_renwu);
        ll_msg_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_mission_new.setVisibility(View.GONE);
                tv_renwu_content.setText("????????????????????????");
                startActivity(new Intent(getActivity(), MsgMissionListActivity.class));
            }
        });

        msgListAdapter.addHeaderView(headerView);

        setOnClickListener(tv_login);

    }

    @Override
    public void onLeftClick(View v) {
        View view = View.inflate(getContext(), R.layout.sqj_pop_msglist_extend, null);
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);//????????????
        popupWindow.setOutsideTouchable(true);//????????????????????????
        view.findViewById(R.id.ll_add_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(getContext(), SearchFriendActivity.class));
            }
        });
        view.findViewById(R.id.ll_creat_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(getContext(), CreatGroupActivity.class));
            }
        });
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, dp2px(getContext(),10 ),dp2px(getContext(),70));
    }

    @Override
    public void onRightClick(View view) {
        tv_right_red.setVisibility(View.GONE);
        startActivity(new Intent(getContext(), MailListActivity.class));

    }

    public void initData() {

        if (SPManager.instance(getActivity()).isLogin()) {
            //??????????????????
            HashMap<String, Object> map = new HashMap<>();
            map.put("cmd", 25);
            String json = new Gson().toJson(map);
            WebSocketManager.getInstance().sendMessage(json);
            //???????????????????????????
            getFriendCount();

            messageObserver = new MessageListener.MessageObserver() {
                @Override
                public void onNewMessage(String text) {
                    int command = WebSocketManager.getCommand(text);
                    if (command == WebSocketManager.RECENT_MESSAGE_RESP) {
                        LogUtil.i("WebSocketManager", "????????????????????????---");
                        ResponseConersationList json = new Gson().fromJson(text, ResponseConersationList.class);
                        getActivity().runOnUiThread(() -> updateConversation(json.data.messageList, true));
                    }else if(command == WebSocketManager.COMMAND_CHAT_REQ){
                        //?????????????????????
                        ResponseMessage responseMessage = new Gson().fromJson(text, ResponseMessage.class);
                        if(responseMessage.data.chatType == 2){

                            List<Conersation> convList = new ArrayList<>();
                            Conersation conersation = new Conersation();
                            conersation.sessionId = responseMessage.data.sessionId;
                            conersation.nickName = responseMessage.data.extras.nickName;
                            conersation.chatBody = responseMessage.data;
                            convList.add(conersation);
                            getActivity().runOnUiThread(() -> updateConversation(convList, true));

                        }
                    }
                }

                @Override
                public void onNewMessage(ByteString bytes) {

                }
            };
            WebSocketManager.getInstance().register(messageObserver);
        }
    }

    /**
     * ??????????????????
     *
     * @param convList ??????????????????
     * @param needSort ????????????????????????
     */
    private void updateConversation(List<Conersation> convList, boolean needSort) {

        for (int i = 0; i < convList.size(); i++) {
            Conersation conv = convList.get(i);
            if(conv.sessionId.equals("n-1")){
                if(conv.unreadMsgNum > 0){
                    tv_gonggao_new.setVisibility(View.VISIBLE);
                    tv_gonggao_new.setText(conv.unreadMsgNum+"");
                    tv_gonggao_content.setText("???????????????????????????");
                }else{
                    tv_gonggao_new.setVisibility(View.GONE);
                    tv_gonggao_content.setText("????????????????????????");
                }
                continue;
            }
            else if(conv.sessionId.equals("n-2")){
                if(conv.unreadMsgNum > 0){
                    tv_mission_new.setVisibility(View.VISIBLE);
                    tv_mission_new.setText(conv.unreadMsgNum+"");
                    tv_renwu_content.setText("?????????????????????????????????");
                }else{
                    tv_mission_new.setVisibility(View.GONE);
                    tv_renwu_content.setText("????????????????????????");
                }
                continue;
            }else if(conv.sessionId.equals("n-3")){
                if(conv.unreadMsgNum > 0){
                    tv_right_red.setVisibility(View.VISIBLE);
                }else{
                    tv_right_red.setVisibility(View.GONE);
                }
                continue;
            }

            boolean isExit = false;
            for (int j = 0; j < msgList.size(); j++) {
                Conersation uiConv = msgList.get(j);
                // UI ???????????????????????????????????????
                if (uiConv.sessionId.equals(conv.sessionId)) {
                    LogUtil.i("WebSocketManager", "UI ???????????????????????????????????????---");
                    msgList.set(j, conv);
                    isExit = true;
                    break;
                }
            }
            // UI ???????????????????????????????????????
            if (!isExit) {
                LogUtil.i("WebSocketManager", "UI ???????????????????????????????????????---");
                msgList.add(conv);
            }
        }
        // 4. ???????????? lastMessage ??? systemTime ??? UI ????????????????????????????????????
        Iterator<Conersation> it = msgList.iterator();
        while (it.hasNext()) {
            Conersation s = it.next();
            if (s.chatBody == null) {
                it.remove();
            }
        }


        if (needSort) {
            LogUtil.i("WebSocketManager", "???????????? lastMessage ??? systemTime ??? UI ????????????????????????????????????---");
            Collections.sort(msgList, new Comparator<Conersation>() {
                @Override
                public int compare(Conersation o1, Conersation o2) {
                    if (o1.isPinned) {
                        return -2;
                    } else
                        return Long.compare(o2.chatBody.systemTime, o1.chatBody.systemTime);
                }
            });
        }

        msgListAdapter.setList(msgList);
    }


    private void getFriendCount(){
        EasyHttp.post(this)
                .api(new FriendApplyCountApi())
                .request(new HttpCallback<HttpData<FriendApplyCountApi.Bean>>(this) {

                    @Override
                    public void onSucceed(HttpData<FriendApplyCountApi.Bean> data) {
                        if(data.getData() != null){
                            FriendApplyCountApi.Bean modle = data.getData();
                            if (modle != null) {
                                long count = modle.count;
                                if (count > 0) {
                                    tv_right_red.setVisibility(View.VISIBLE);
                                } else {
                                    tv_right_red.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @SingleClick
    @Override
    public void onClick(View v) {
       int id = v.getId();
       if (id == R.id.tv_login) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().remove(messageObserver);
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Conersation conersation = msgList.get(position);
        long targetUid = 0;
        boolean isGroup = false;
        if (conersation.chatBody == null) {

        } else {
            if(conersation.chatBody.chatType == 2){
                isGroup = false;
                targetUid = conersation.chatBody.from == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId ?
                        conersation.chatBody.to : conersation.chatBody.from;
            }else if(conersation.chatBody.chatType == 1){
                isGroup = true;
                targetUid = conersation.chatBody.groupId;
            }

            //toast(targetUid+"");


            HashMap<String, Object> map = new HashMap<>();

            WebSocketManager.getInstance().read(msgList.get(position).sessionId);
            msgList.get(position).unreadMsgNum = 0;
            /**
             * java.lang.IllegalArgumentException: Called attach on a child which is not detached: ViewHolder
             *
             * RecyclerView?????????Header ????????????????????????????????? position == 0 ???Header????????????
             */
            //msgListAdapter.notifyItemChanged(position);
            /**
             * ???????????????
             */
            msgListAdapter.notifyItemChanged(position + 1);
            // ?????????????????????
            TempMessageActivity.show(getAttachActivity(), targetUid,
                    conersation.sessionId, conersation.nickName, isGroup, new TempMessageActivity.OnFinishResultListener() {
                        @Override
                        public void onSucceed(String data, boolean isLogout) {
                            if(isLogout){
                                LogUtil.d("diskkiller","TempMessageActivity group_logout==="+ data);
                                Iterator<Conersation> it = msgList.iterator();
                                while (it.hasNext()) {
                                    Conersation s = it.next();
                                    if (s.chatBody != null && (s.chatBody.groupId+"").equals(data)) {
                                        it.remove();
                                    }
                                }

                                msgListAdapter.setList(msgList);
                            }
                        }
                    });

            /*MsgActivity.start(getAttachActivity(), targetUid,conersation.sessionId,  conersation.nickName, new MsgActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {
                    //????????????
                    initData();
                }
            });*/

        }
    }
}
