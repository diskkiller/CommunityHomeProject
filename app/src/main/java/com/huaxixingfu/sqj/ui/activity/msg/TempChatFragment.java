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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.app.AppFragment;
import com.huaxixingfu.sqj.bean.Message;
import com.huaxixingfu.sqj.push.bean.RequestMessage;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.ui.adapter.MsgAdapter;
import com.huaxixingfu.sqj.widget.tuita.PanelFragment;


import java.util.ArrayList;


/**
 */
public abstract class TempChatFragment
        extends AppFragment
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


    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
    }


    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_chat_common;
    }
    protected  int initview() {
        return R.layout.fragment_chat_common;
    }

    @Override
    protected void initWidget(View root) {
        //getActivity().getWindow().setBackgroundDrawableResource(R.drawable.bg_src_girl);

        // 在这里进行了控件绑定
        super.initWidget(root);

        mRecyclerView = root.findViewById(R.id.recycler);
        mContent = root.findViewById(R.id.edit_content);
        mSubmit = root.findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });
        say = root.findViewById(R.id.im_longClickToSay);
        write = root.findViewById(R.id.im_key_layout);
        record = root.findViewById(R.id.btn_record);


        initEditContent();

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        // 初始化面板操作
        mPanelBoss = (AirPanel.Boss) root.findViewById(R.id.lay_content);
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
    }

    private void onBottomPanelOpened() {

        mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
        Log.i("datainfo","ChatFragment onBottomPanelOpened  scrollToPosition");

    }

    @Override
    public boolean onBackPressed() {
        if (mPanelBoss.isOpen()) {
            // 关闭面板并且返回true代表自己已经处理了消费了返回
            mPanelBoss.closePanel();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected void initData() {
        super.initData();
        // 开始进行初始化操作

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
            String content = mContent.getText().toString();

            RequestMessage requestMessage = new RequestMessage();
            requestMessage.cmd = WebSocketManager.COMMAND_CHAT_REQ;
            requestMessage.content = content;
            requestMessage.from = 111;
            requestMessage.to = 123;
            requestMessage.chatType = 2;
            requestMessage.msgType = 0;
            requestMessage.createTime = System.currentTimeMillis();
            mContent.setText("");
            scrollToEnd(requestMessage);

            RequestMessage requestMessage1 = new RequestMessage();
            requestMessage1.cmd = WebSocketManager.COMMAND_CHAT_REQ;
            requestMessage1.content = content;
            requestMessage1.from = 222;
            requestMessage1.to = 123;
            requestMessage1.chatType = 2;
            requestMessage1.msgType = 0;
            requestMessage1.createTime = System.currentTimeMillis();
            mContent.setText("");
            scrollToEnd(requestMessage1);


        } else {
            onMoreClick();
        }
    }

    private long msgTime;
    private void scrollToEnd(RequestMessage msg) {
        //区分时间戳，添加新消息
        if (msg.systemTime - msgTime > 180000) {
            msgTime = msg.systemTime;
            mAdapter.addData(new Message(msg, true));
        } else {
            mAdapter.addData(new Message(msg, false));
        }
        mRecyclerView.scrollToPosition(msgList.size() - 1);
    }

    private void onMoreClick() {
        mPanelBoss.openPanel();
        //mPanelFragment.showGallery();
        mPanelFragment.showMore();
    }


    @Override
    public EditText getInputEditText() {
        // 返回输入框
        return mContent;
    }


}
