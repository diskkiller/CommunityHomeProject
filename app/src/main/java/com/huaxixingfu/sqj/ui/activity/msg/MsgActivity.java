package com.huaxixingfu.sqj.ui.activity.msg;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.diskkiller.base.BaseActivity;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.aop.SingleClick;
import com.huaxixingfu.sqj.app.AppActivity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.bean.Message;
import com.huaxixingfu.sqj.bean.UserData;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.commom.IntentKey;
import com.huaxixingfu.sqj.push.bean.RequestMessage;
import com.huaxixingfu.sqj.push.bean.RequestMessageRecord;
import com.huaxixingfu.sqj.push.bean.ResponseMessage;
import com.huaxixingfu.sqj.push.bean.ResponseMessageRecord;
import com.huaxixingfu.sqj.push.manager.MessageListener;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.ui.adapter.MsgAdapter;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;
import com.youth.banner.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okio.ByteString;

/**
 * @Description: java类作用描述
 */
public class MsgActivity extends AppActivity {

    private RecyclerView rv_msg;
    private ArrayList<Message> msgList;
    private MessageListener.MessageObserver messageObserver;
    private MsgAdapter msgAdapter;
    private TextView tv_send;
    private EditText et_text;
    private long msgTime;
    private LinearLayout ll_msg;
    private View.OnLayoutChangeListener layoutChangeListener;
    private int page = 0;
    private long targetUid;
    private String sessionId;
    private String nickName;
    private TextView tv_title;
    private TitleBar titleBar;

    public static void start(BaseActivity activity, long targetUid,String sessionId, String nickName,OnFinishResultListener listener) {
        Intent intent = new Intent(activity, MsgActivity.class);
        intent.putExtra(IntentKey.TARGETUID, targetUid);
        intent.putExtra(IntentKey.SESSIONID, sessionId);
        intent.putExtra(IntentKey.NICKNAME, nickName);
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
        return R.layout.sqj_activity_msg;
    }

    @Override
    protected void initView() {
        titleBar = findViewById(R.id.tb_title);
        rv_msg = findViewById(R.id.rv_msg);
        tv_send = findViewById(R.id.tv_send);
        et_text = findViewById(R.id.et_text);
        ll_msg = findViewById(R.id.ll_msg);

        setOnClickListener(R.id.tv_send);

        initIntent();
        initET();
        initRV();
        initObs();
    }

    @Override
    public void onRightClick(View view) {
        //FriendInfoActivity.start(MsgActivity.this,targetUid,nickName,null);
    }

    private void initIntent() {
        targetUid = getLong(IntentKey.TARGETUID, 0);
        sessionId = getString(IntentKey.SESSIONID);
        nickName = getString(IntentKey.NICKNAME);
        titleBar.setTitle(nickName);
        LogUtil.e("aaaaaaaa", "aaaaaa--" + targetUid + "---aaaaaaa----" + sessionId);
    }

    public void initData() {

        RequestMessageRecord messageRecord = new RequestMessageRecord();
        messageRecord.from = SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA, UserData.class).userId;
        messageRecord.to = targetUid;
        messageRecord.cmd = WebSocketManager.COMMAND_GET_MESSAGE_REQ;
        messageRecord.current = page;
        messageRecord.size = 20;
        String json = new Gson().toJson(messageRecord);
        WebSocketManager.getInstance().sendMessage(json);
    }

    private void initRV() {

        msgList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv_msg.setLayoutManager(llm);
        msgAdapter = new MsgAdapter(msgList);

        rv_msg.setAdapter(msgAdapter);
        //上拉加载

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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置已读
                            WebSocketManager.getInstance().read(sessionId);
                            scrollToEnd(responseMessage.data);
                        }
                    });
                } else if (command == WebSocketManager.COMMAND_GET_MESSAGE_RESP) {
                    //如果是拉取历史记录
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置历史
                            msgAdapter.getUpFetchModule().setUpFetching(false);
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

        //键盘弹起的监听
        layoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {
                    //键盘展开
                    //滑动到最后一条
                    rv_msg.scrollToPosition(msgList.size() - 1);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 0)) {
                    //键盘隐藏
                }
            }
        };

        ll_msg.addOnLayoutChangeListener(layoutChangeListener);
    }

    /**
     * 滑動到最後
     *
     * @param msg
     */
    private void scrollToEnd(RequestMessage msg) {
        //只有当前聊天列表的才处理
        if ((msg.from == targetUid || msg.from == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId)
                && (msg.to == targetUid || msg.to == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId)) {
            //区分时间戳，添加新消息
            if (msg.systemTime - msgTime > 180000) {
                msgTime = msg.systemTime;
                msgAdapter.addData(new Message(msg, true));
            } else {
                msgAdapter.addData(new Message(msg, false));
            }
            rv_msg.scrollToPosition(msgList.size() - 1);
        }
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
        msgAdapter.setList(msgList);
        rv_msg.scrollToPosition(msgList.size() - 1);
//        } else {//获取更多以历史
//            msgAdapter.addData(0, msgList);
//        }

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_send) {
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.cmd = WebSocketManager.COMMAND_CHAT_REQ;
            requestMessage.content = et_text.getText().toString();
            requestMessage.from = SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId;
            requestMessage.to = targetUid;
            requestMessage.chatType = 2;
            requestMessage.msgType = 0;
            requestMessage.createTime = System.currentTimeMillis();
            WebSocketManager.getInstance().sendMessage(new Gson().toJson(requestMessage));
            et_text.setText("");
        }
    }

    /**
     * 编辑聊天内容的初始化，最大限制五百字
     */
    private void initET() {

        et_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_send.setVisibility(View.VISIBLE);
                } else {
                    tv_send.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageObserver != null) {
            WebSocketManager.getInstance().remove(messageObserver);
        }
        setResult(RESULT_OK, new Intent().putExtra(IntentKey.STRING_DATE, sessionId));
    }
}
