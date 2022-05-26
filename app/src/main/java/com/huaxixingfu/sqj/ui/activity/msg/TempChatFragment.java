package com.huaxixingfu.sqj.ui.activity.msg;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.app.TitleBarFragment;
import com.huaxixingfu.sqj.bean.Message;
import com.huaxixingfu.sqj.bean.UserData;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.push.bean.Conersation;
import com.huaxixingfu.sqj.push.bean.RequestMessage;
import com.huaxixingfu.sqj.push.bean.RequestMessageRecord;
import com.huaxixingfu.sqj.push.bean.ResponseMessage;
import com.huaxixingfu.sqj.push.bean.ResponseMessageRecord;
import com.huaxixingfu.sqj.push.manager.MessageListener;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.ui.adapter.MsgAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;
import com.huaxixingfu.sqj.widget.tuita.PanelFragment;
import com.huaxixingfu.sqj.widget.tuita.TextWatcherAdapter;
import com.huaxixingfu.sqj.widget.tuita.airpanel.AirPanel;
import com.huaxixingfu.sqj.widget.tuita.airpanel.Util;
import com.huaxixingfu.sqj.widget.tuita.recycler.RecyclerAdapter;
import com.luck.picture.lib.tools.ToastUtils;
import com.tencent.liteav.trtccalling.TUICalling;
import com.tencent.liteav.trtccalling.TUICallingImpl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okio.ByteString;


/**
 */
public abstract class TempChatFragment
        extends TitleBarFragment<AppActivity>
        implements AppBarLayout.OnOffsetChangedListener, PanelFragment.PanelCallback,SensorEventListener {

    private static final String TAG = "ChatFragment";
    protected String mReceiverId;
    protected MsgAdapter mAdapter;
    private int  PAGE = 1;

    RecyclerView mRecyclerView;


    EditText mContent;

    View mSubmit;

    Button say;

    LinearLayout write;

    ImageButton record;

    // 控制顶部面板与软键盘过度的Boss控件
    private AirPanel.Boss mPanelBoss;
    private PanelFragment mPanelFragment;

    private boolean mKeyboardOpen;

    private NotificationManager notificationManager;
    private LinearLayoutManager layout;

    private ArrayList<Message> msgList;

    private long targetUid;
    private String sessionId;
    private String nickName;
    private boolean mIsGroup;

    private int page = 0;

    private MessageListener.MessageObserver messageObserver;

    private TitleBar titleBar;


    @Override
    protected int getLayoutId() {
        return R.layout.tuita_fragment_chat_common;
    }

    @Override
    protected void initView() {

        titleBar = (TitleBar) findViewById(R.id.tb_title);

        targetUid = getLong(IntentKey.TARGETUID, 0);
        sessionId = getString(IntentKey.SESSIONID);
        nickName = getString(IntentKey.NICKNAME);
        mIsGroup = getBoolean(IntentKey.KEY_IS_GROUP);

        titleBar.setTitle(nickName);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mContent = (EditText) findViewById(R.id.edit_content);
        mSubmit = findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });
        say = (Button) findViewById(R.id.im_longClickToSay);
        write = (LinearLayout) findViewById(R.id.im_key_layout);
        record = (ImageButton) findViewById(R.id.btn_record);


        initEditContent();

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        // 初始化面板操作
        mPanelBoss = (AirPanel.Boss) findViewById(R.id.lay_content);
        mPanelBoss.setup(new AirPanel.PanelListener() {
            @Override
            public void requestHideSoftKeyboard() {
                // 请求隐藏软键盘
                Util.hideKeyboard(mContent);
            }
        });
        mPanelBoss.setOnStateChangedListener(new AirPanel.OnStateChangedListener() {
            @Override
            public void onPanelStateChanged(boolean isOpen) {
                // 面板改变
                if (isOpen)
                    onBottomPanelOpened();
            }

            @Override
            public void onSoftKeyboardStateChanged(boolean isOpen) {
                // 软键盘改变
                mKeyboardOpen = isOpen;
                Log.i(TAG, "软键盘改变:  "+mKeyboardOpen);
                if (isOpen)
                    onBottomPanelOpened();
            }
        });
        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        mPanelFragment.setup(this);


        // RecyclerView基本设置
        layout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layout);
        msgList = new ArrayList<>();
        mAdapter = new MsgAdapter(msgList);
        mRecyclerView.setAdapter(mAdapter);
        // 添加适配器监听器，进行点击的实现
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mPanelBoss.isOpen()) {
                    // 关闭面板并且返回true代表自己已经处理了消费了返回
                    mPanelBoss.closePanel();
                    return;
                }else if(mKeyboardOpen){
                    // 请求隐藏软键盘
                    Util.hideKeyboard(mContent);
                    return;
                }
            }
        });

        initObs();
    }



    @Override
    public void onRightClick(View view) {
        if(mIsGroup){
            GroupSettingActivity.start(getAttachActivity(), targetUid, new GroupSettingActivity.OnFinishResultListener() {
                @Override
                public void onSucceed(String data) {

                }
            });
        }else{
            ChatSettingActivity.start(getAttachActivity(),targetUid,nickName,null);
        }
    }

    @Override
    public void onLeftClick(View view) {
        if (mPanelBoss.isOpen()) {
            // 关闭面板并且返回true代表自己已经处理了消费了返回
            mPanelBoss.closePanel();
        }
        finish();
    }

    @Override
    protected void initData() {
        RequestMessageRecord messageRecord = new RequestMessageRecord();

        if(!mIsGroup){
            messageRecord.from = SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA, UserData.class).userId;
            messageRecord.to = targetUid;
        }else{
            messageRecord.groupId = targetUid;
        }

        messageRecord.cmd = WebSocketManager.COMMAND_GET_MESSAGE_REQ;
        messageRecord.current = page;
        messageRecord.size = 20;
        String json = new Gson().toJson(messageRecord);
        WebSocketManager.getInstance().sendMessage(json);
    }

    private void initObs() {

        messageObserver = new MessageListener.MessageObserver() {
            @Override
            public void onNewMessage(String text) {
                LogUtil.e("aaaaaaaMsgAcivity", "aaaaaaa----" + text);
                int command = WebSocketManager.getCommand(text);
                if (command == WebSocketManager.COMMAND_CHAT_REQ) {
                    //如果是单条消息
                    ResponseMessage responseMessage = new Gson().fromJson(text, ResponseMessage.class);
                    getAttachActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isExit = false;
                            for (int j = 0; j < mAdapter.getData().size(); j++) {
                                Message message = mAdapter.getData().get(j);
                                // UI 会话列表存在该会话，则替换
                                if (message.msg.id.equals(responseMessage.data.id)) {
                                    isExit = true;
                                    break;
                                }
                            }
                            // UI 会话列表没有该会话，则新增
                            if (!isExit) {
                                //设置已读
                                WebSocketManager.getInstance().read(sessionId);
                                scrollToEnd(responseMessage.data);
                            }

                        }
                    });
                } else if (command == WebSocketManager.COMMAND_GET_MESSAGE_RESP) {
                    //如果是拉取历史记录
                    getAttachActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置历史
                            mAdapter.getUpFetchModule().setUpFetching(false);
                            ArrayList<Message> messages = new ArrayList<>();
                            ResponseMessageRecord record = new Gson().fromJson(text, ResponseMessageRecord.class);
                            //循环包装
                            for (RequestMessage msg : record.data.bodies) {
                                Message msgBean = new Message(msg);
                                messages.add(msgBean);
                            }
                            updateMsgList(messages);
                        }
                    });
                }
            }

            @Override
            public void onNewMessage(ByteString bytes) {

            }
        };

        WebSocketManager.getInstance().register(messageObserver);

    }

    /**
     * 重新排序消息列表
     *
     * @param msgList C2C数据集合
     */
    private void updateMsgList(List<Message> msgList) {

        // 按照会话 msgList 的 timestamp 对 Msg 列表做排序并更新界面
        Collections.sort(msgList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if (o1.msg.systemTime > o2.msg.systemTime) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        //计算时间戳是否需要展示，三分钟以内连续有回复，则隐藏，三分钟以外则展示一个时间戳
        if (msgList.size() > 0) {
            //首个，必定展示
            msgList.get(0).timeFlag = true;
            long timestamp = msgList.get(0).msg.systemTime;
            for (Message msg : msgList) {
                //该单位是秒
                if (msg.msg.systemTime - timestamp > 180000) {
                    msg.timeFlag = true;
                    timestamp = msg.msg.systemTime;
                }
            }
        }

//        if (page == 0) {//首次加载
        mAdapter.setList(msgList);
        mRecyclerView.scrollToPosition(msgList.size() - 1);
//        } else {//获取更多以历史
//            msgAdapter.addData(0, msgList);
//        }

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (messageObserver != null) {
            WebSocketManager.getInstance().remove(messageObserver);
        }
    }

    private void onBottomPanelOpened() {

        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        Log.i("datainfo","ChatFragment onBottomPanelOpened  scrollToPosition");

    }


    public boolean onBackPressed() {
        if (mPanelBoss.isOpen()) {
            // 关闭面板并且返回true代表自己已经处理了消费了返回
            mPanelBoss.closePanel();
            return true;
        }
        return false;
    }



    // 初始化输入框监听
    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                // 设置状态，改变对应的Icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }


    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            // 发送

            RequestMessage requestMessage = new RequestMessage();
            requestMessage.cmd = WebSocketManager.COMMAND_CHAT_REQ;
            requestMessage.content = mContent.getText().toString();
            requestMessage.msgType = 0;
            requestMessage.createTime = System.currentTimeMillis();
            requestMessage.from = SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA, UserData.class).userId;


            if(!mIsGroup){
                requestMessage.to = targetUid;
                requestMessage.chatType = 2;
            }else{
                requestMessage.groupId = targetUid;
                requestMessage.chatType = 1;
            }

            WebSocketManager.getInstance().sendMessage(new Gson().toJson(requestMessage));
            mContent.setText("");



            //scrollToEnd(requestMessage);
        } else {
            onMoreClick();
        }
    }

    private long msgTime;
    private void scrollToEnd(RequestMessage msg) {
        LogUtil.e("diskkiller","msg.from == "+msg.from+"  "
                +"msg.to == "+msg.to+"  "+
                "myUserId == "+SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId);
        //只有当前聊天列表的才处理
        /*if ((msg.from == targetUid || msg.from == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId)
                && (msg.to == targetUid || msg.to == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId)) {
            //区分时间戳，添加新消息
            if (msg.systemTime - msgTime > 180000) {
                msgTime = msg.systemTime;
                mAdapter.addData(new Message(msg, true));
            } else {
                mAdapter.addData(new Message(msg, false));
            }
            mRecyclerView.scrollToPosition(msgList.size() - 1);
        }*/

        if(mIsGroup){
            if (msg.groupId == targetUid) {
                //区分时间戳，添加新消息
                if (msg.systemTime - msgTime > 180000) {
                    msgTime = msg.systemTime;
                    mAdapter.addData(new Message(msg, true));
                } else {
                    mAdapter.addData(new Message(msg, false));
                }
                mRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        }else{
            if (msg.to == targetUid  || msg.from == targetUid) {
                //区分时间戳，添加新消息
                if (msg.systemTime - msgTime > 180000) {
                    msgTime = msg.systemTime;
                    mAdapter.addData(new Message(msg, true));
                } else {
                    mAdapter.addData(new Message(msg, false));
                }
                mRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        }

    }

    private void onMoreClick() {
        mPanelBoss.openPanel();
        //mPanelFragment.showGallery();
        mPanelFragment.showMore();
    }

    public static final int TYPE_AUDIO_CALL       = 1;
    public static final int TYPE_VIDEO_CALL       = 2;

    @Override
    public void onGotoCallVideo() {
        if(mIsGroup){
            CreatGroupCallingActivity.start(getAttachActivity(),TYPE_VIDEO_CALL,null);

        }else{

            TUICallingImpl.sharedInstance(getApplication()).enableFloatWindow(true);
            TUICallingImpl.sharedInstance(getApplication()).call(new String[]{targetUid+""}, TUICalling.Type.VIDEO);
        }
    }

    @Override
    public void onGotoCallVoice() {
        if(mIsGroup){
            CreatGroupCallingActivity.start(getAttachActivity(),TYPE_AUDIO_CALL,null);

        }else{

            TUICallingImpl.sharedInstance(getApplication()).enableFloatWindow(true);
            TUICallingImpl.sharedInstance(getApplication()).call(new String[]{targetUid+""}, TUICalling.Type.AUDIO);
        }
    }

    @Override
    public EditText getInputEditText() {
        // 返回输入框
        return mContent;
    }


}
