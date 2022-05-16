package com.huaxixingfu.sqj.push.manager;

import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.push.bean.RequestHeartBeat;
import com.huaxixingfu.sqj.push.bean.ResponseMessage;
import com.huaxixingfu.sqj.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class WebSocketManager implements MessageListener.MessageObserverable {


    /**
     * 未知命令
     */
    public static final int COMMAND_UNKNOWN = 0;
    /**
     * 握手请求，含http的websocket握手请求
     */
    public static final int COMMAND_HANDSHAKE_REQ = 1;

    /**
     * 握手响应，含http的websocket握手响应
     */
    public static final int COMMAND_HANDSHAKE_RESP = 2;

    /**
     * 鉴权请求
     */
    public static final int COMMAND_AUTH_REQ = 3;

    /**
     * 鉴权响应
     */
    public static final int COMMAND_AUTH_RESP = 4;

    /**
     * 登录请求
     */
    public static final int COMMAND_LOGIN_REQ = 5;

    /**
     * 登录响应
     */
    public static final int COMMAND_LOGIN_RESP = 6;

    /**
     * 登录成功的code
     */
    public static final int LOGIN_SUCCESS = 10007;


    /**
     * 申请进入群组
     */
    public static final int COMMAND_JOIN_GROUP_REQ = 7;

    /**
     * 申请进入群组响应
     */
    public static final int COMMAND_JOIN_GROUP_RESP = 8;

    /**
     * 进入群组通知
     */
    public static final int COMMAND_JOIN_GROUP_NOTIFY_RESP = 9;

    /**
     * <pre>
     * 退出群组通知
     * </pre>
     *
     * <code>COMMAND_EXIT_GROUP_NOTIFY_RESP = 10;</code>
     */
    public static final int COMMAND_EXIT_GROUP_NOTIFY_RESP = 10;

    /**
     * 聊天请求
     */
    public static final int COMMAND_CHAT_REQ = 11;

    /**
     * 聊天响应
     */
    public static final int COMMAND_CHAT_RESP = 12;

    /**
     * 心跳请求
     */
    public static final int OMMAND_HEARTBEAT_REQ = 13;

    /**
     * 关闭请求
     */
    public static final int COMMAND_CLOSE_REQ = 14;

    /**
     * 发出撤消消息指令(管理员可以撤消所有人的消息，自己可以撤消自己的消息)
     */
    public static final int COMMAND_CANCEL_MSG_REQ = 15;

    /**
     * 收到撤消消息指令
     */
    public static final int COMMAND_CANCEL_MSG_RESP = 16;

    /**
     * 获取用户信息;
     */
    public static final int COMMAND_GET_USER_REQ = 17;

    /**
     * 获取用户信息响应;
     */
    public static final int COMMAND_GET_USER_RESP = 18;

    /**
     * 获取聊天消息;
     */
    public static final int COMMAND_GET_MESSAGE_REQ = 19;

    /**
     * 获取聊天消息响应;
     */
    public static final int COMMAND_GET_MESSAGE_RESP = 20;

    /**
     * 确认消息请求;
     */
    public static final int COMMAND_ACK_MESSAGE_REQ = 21;

    /**
     * 确认消息响应;
     */
    public static final int COMMAND_ACK_MESSAGE_RESP = 22;

    /**
     * 获取离线请求;
     */
    public static final int COMMAND_OFFLINE_MESSAGE_REQ = 23;

    /**
     * 获取离线响应;
     */
    public static final int COMMAND_OFFLINE_MESSAGE_RESP = 24;

    /**
     * 消息列表请求
     */
    public static final int RECENT_MESSAGE_REQ = 25;
    /**
     * 消息列表响应
     */

    public static final int RECENT_MESSAGE_RESP = 26;

    /**
     * 消息已读确认请求
     */
    public static final int MESSAGE_READ_REQ = 27;

    /**
     * 消息已读确认响应
     */

    public static final int MESSAGE_READ_RESP = 28;


    private List<MessageListener.MessageObserver> messageObserverList; //观察者List

    private final static int MAX_NUM = 10;       // 最大重连数
    private final static int MILLIS = 5000;     // 重连间隔时间，毫秒
    private static WebSocketManager mInstance = null;

    private OkHttpClient client;
    private Request request;
    private ConnectStateListener connectStateListener;
    private WebSocket mWebSocket;

    private boolean isConnect = false;
    private int connectNum = 0;

    //心跳包发送时间计时
    private long sendTime = 0L;
    // 发送心跳包
    private final Handler mHandler = new Handler();
    // 每隔30秒发送一次心跳包，检测连接没有断开
    private static final long HEART_BEAT_RATE = 30 * 1000;

    private WebSocketManager() {

    }

    @Override
    public void register(MessageListener.MessageObserver messageObserver) {
        if (messageObserverList == null || messageObserver == null) {
            return;
        }
        synchronized (this) {
            if (!messageObserverList.contains(messageObserver)) {
                messageObserverList.add(messageObserver);
            }
        }
    }

    @Override
    public void remove(MessageListener.MessageObserver messageObserver) {
        if (messageObserverList == null || messageObserver == null) {
            return;
        }
        messageObserverList.remove(messageObserver);
    }

    @Override
    public void message(String text) {
        for (MessageListener.MessageObserver observer : messageObserverList) {
            observer.onNewMessage(text);
        }
    }

    @Override
    public void message(ByteString bytes) {
        for (MessageListener.MessageObserver observer : messageObserverList) {
            observer.onNewMessage(bytes);
        }
    }

    public interface ConnectStateListener {

        void onConnectSuccess();// 连接成功

        void onConnectFailed();// 连接失败

        void onClose(); // 关闭

        void onMessage(String text);//文本
    }


    public static WebSocketManager getInstance() {
        if (null == mInstance) {
            synchronized (WebSocketManager.class) {
                if (mInstance == null) {
                    mInstance = new WebSocketManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取判定码
     *
     * @param text 目标信息
     * @return 结果 如果获取不到，返回-1
     */
    public static int getCommand(String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            return jsonObject.getInt("command");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取判定码
     *
     * @param text 目标信息
     * @return 结果 如果获取不到，返回-1
     */
    public static int getCode(String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            return jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 释放单例, 及其所引用的资源
     */
    public static void release() {
        try {
            if (mInstance != null) {
                mInstance = null;
            }
        } catch (Exception e) {
            LogUtil.e("aaaaaaaWebSocketManager", "release : " + e.toString());
        }
    }

    public void init(String token, ConnectStateListener message) {
        messageObserverList = new ArrayList<>();
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        request = new Request.Builder().url(BuildConfig.HOST_WEBSOCKET_URL+ token).build();
        LogUtil.e("diskkiller","websocket 地址：："+BuildConfig.HOST_WEBSOCKET_URL+ token);
        connectStateListener = message;
        connect();
    }

    // 发送心跳包
    String heartBeat = new Gson().toJson(new RequestHeartBeat(13, -128));
    private final Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                sendTime = System.currentTimeMillis();
                boolean isSend = sendMessage(heartBeat);
                LogUtil.e("aaaaaaaWebSocketManager", "心跳是否发送成功---" + isSend);
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE); //每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    /**
     * 连接
     */
    public void connect() {
        if (isConnect()) {
            LogUtil.e("aaaaaaaWebSocketManager", "WebSocket 已经连接！");
            return;
        }
        client.newWebSocket(request, createListener());
    }

    /**
     * 尝试重连
     */
    public WebSocketManager reconnect() {
        if (connectNum <= MAX_NUM) {
            try {
                Thread.sleep(MILLIS);
                connect();
                connectNum++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LogUtil.i("aaaaaaaWebSocketManager", "reconnect over " + MAX_NUM + ",please check url or network");
        }
        return this;
    }

    /**
     * 刷新重连次数
     */
    public WebSocketManager refreshReconnectCount() {
        connectNum = 0;
        return this;
    }

    /**
     * 是否已经达到最大重连接次数
     *
     * @return
     */
    public boolean isMaxReconnectCount() {
        return connectNum == MAX_NUM;
    }

    /**
     * 添加连接状态监听 ，一般是后续用来再次重连后的监听
     *
     * @param connectStateListener
     */
    public void addOnConnectStateChangeListener(ConnectStateListener connectStateListener) {
        this.connectStateListener = connectStateListener;
    }

    /**
     * 是否连接
     */
    public boolean isConnect() {
        return mWebSocket != null && isConnect;
    }

    /**
     * 发送消息
     *
     * @param text 字符串
     * @return boolean
     */
    public boolean sendMessage(String text) {
        LogUtil.e("websocket isConnect ", "======"+isConnect());
        if (!isConnect()) return false;
        LogUtil.e("aaaaaaWebSocketManagerSend", text);
        return mWebSocket.send(text);
    }

    /**
     * 发送消息
     *
     * @param byteString 字符集
     * @return boolean
     */
    public boolean sendMessage(ByteString byteString) {
        if (!isConnect()) return false;
        return mWebSocket.send(byteString);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (isConnect()) {
            mWebSocket.cancel();
            mWebSocket.close(1001, "客户端主动关闭连接");
        }
    }

    private WebSocketListener createListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                LogUtil.e("aaaaaaaWebSocketManager", "WebSocket 打开:" + response.toString());
                mWebSocket = webSocket;
                isConnect = response.code() == 101;
                if (!isConnect) {
                    reconnect();
                } else {
                    LogUtil.e("aaaaaaaWebSocketManager", "WebSocket 连接成功");
                    if (connectStateListener != null) {
                        connectStateListener.onConnectSuccess();
                    }
                    //发送心跳
                    mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                //消息默认确认
                if (getCommand(text) == COMMAND_CHAT_REQ) {
                    ResponseMessage json = new Gson().fromJson(text, ResponseMessage.class);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("cmd", COMMAND_ACK_MESSAGE_REQ);
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(json.data.id);
                    map.put("ids", strings);
                    String s = new Gson().toJson(map);
                    LogUtil.e("aaaaaaaaSocketManager", "消息确认------" + s);
                    sendMessage(s);
                }

                LogUtil.e("aaaaaaWebSocketManagerReceive", text);

                if (connectStateListener != null) {
                    connectStateListener.onMessage(text);
                }
                //传递出去
                message(text);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
                //传递出去
                message(bytes);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
                mWebSocket = null;
                isConnect = false;
                if (connectStateListener != null) {
                    connectStateListener.onClose();
                }
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
                mWebSocket = null;
                isConnect = false;
                if (connectStateListener != null) {
                    connectStateListener.onClose();
                }
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                if (response != null) {
                    LogUtil.e("aaaaaaaWebSocketManager", "WebSocket 连接失败：" + response.message());
                }
                LogUtil.e("aaaaaaaWebSocketManager", "WebSocket 连接失败异常原因：" + t.getMessage());
                isConnect = false;
                if (connectStateListener != null) {
                    connectStateListener.onConnectFailed();
                }

//                if (!TextUtils.isEmpty(t.getMessage()) && !Objects.equals(t.getMessage(), "Socket closed")) {
//                    reconnect();
//                }

                if (!TextUtils.isEmpty(t.getMessage()) && !Objects.equals(t.getMessage(), "Socket closed")) {
                    reconnect();
                }
            }
        };
    }

    /**
     * 设置已读
     *
     * @param ids sessionIds 会话id集合
     */
    public void read(List<String> ids) {
        if (!isConnect()) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", MESSAGE_READ_REQ);
        map.put("sessionIds", ids);
        String json = new Gson().toJson(map);
        sendMessage(json);
    }

    /**
     * 设置已读
     *
     * @param id sessionId 回话id
     */
    public void read(String id) {
        if (!isConnect()) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("cmd", MESSAGE_READ_REQ);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);
        map.put("sessionIds", ids);
        String json = new Gson().toJson(map);
        sendMessage(json);
    }
}
