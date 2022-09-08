/*
 * Copyright (C) 2021  即时通讯网(52im.net) & Jack Jiang.
 * The MobileIMSDK_TCP (MobileIMSDK v6.x TCP版) Project. 
 * All rights reserved.
 * 
 * > Github地址：https://github.com/JackJiang2011/MobileIMSDK
 * > 文档地址：  http://www.52im.net/forum-89-1.html
 * > 技术社区：  http://www.52im.net/
 * > 技术交流群：215477170 (http://www.52im.net/topic-qqgroup.html)
 * > 作者公众号：“即时通讯技术圈】”，欢迎关注！
 * > 联系作者：  http://www.52im.net/thread-2792-1-1.html
 *  
 * "即时通讯网(52im.net) - 即时通讯开发者社区!" 推荐开源工程。
 * 
 * LocalSocketProvider.java at 2021-7-1 15:08:17, code by Jack Jiang.
 */
package diskkiller.core;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import diskkiller.ClientCoreSDK;
import diskkiller.conf.ConfigEntity;
import diskkiller.utils.MBObserver;
import diskkiller.utils.MBThreadPoolExecutor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class LocalSocketProvider {
    private final static String TAG = LocalSocketProvider.class.getSimpleName();

    public static int TCP_FRAME_FIXED_HEADER_LENGTH = 4;     // 4 bytes
    public static int TCP_FRAME_MAX_BODY_LENGTH = 6 * 1024; // 6K bytes

    private static LocalSocketProvider instance = null;


    private MBObserver connectionDoneObserver;
    private boolean isConnect;

    public static LocalSocketProvider getInstance() {
        if (instance == null)
            instance = new LocalSocketProvider();
        return instance;
    }

    private LocalSocketProvider() {
    }

    private OkHttpClient client;
    private Request request;
    private WebSocket mWebSocket;

    private void initLocalBootstrap() {
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
            request = new Request.Builder().url(ConfigEntity.serverIP).build();

        } catch (Exception e) {
            Log.w(TAG, "localWebSocket初始化时出错，原因是：" + e.getMessage(), e);
        }
    }

    public void setConnectionDoneObserver(MBObserver connectionDoneObserver) {
        this.connectionDoneObserver = connectionDoneObserver;
    }

    public WebSocket resetLocalSocket() {
        try {
            closeLocalSocket();
            initLocalBootstrap();
            tryConnectToHost();

            return mWebSocket;
        } catch (Exception e) {
            Log.w(TAG, "【IMCORE-TCP】重置localSocket时出错，原因是：" + e.getMessage(), e);
            closeLocalSocket();
            return null;
        }
    }

    private boolean tryConnectToHost() {
        if (ClientCoreSDK.DEBUG)
            Log.d(TAG, "【IMCORE-TCP】tryConnectToHost并获取connection开始了...");

        try {
            client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                    mWebSocket = webSocket;
                    isConnect = response.code() == 101;

                    if (!isConnect) {
                        Log.w(TAG, "【IMCORE-tryConnectToHost-异步回调】连接失败，原因是："+response.message());
                    } else {
                        if (ClientCoreSDK.DEBUG) {
                            Log.d(TAG, "【IMCORE-netty-channelActive】连接已成功建立！(isLocalSocketReady=" + isLocalSocketReady() + ")");
                        }
                    }

                    if (LocalSocketProvider.this.connectionDoneObserver != null) {
                        connectionDoneObserver.update(isConnect, null);
                        LocalSocketProvider.this.connectionDoneObserver = null;
                    }

                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    //接收服务端的消息
                    //文本信息
                    System.out.println("客户端接收的消息是:"+text);
                    LocalDataReciever.getInstance().handleProtocal(text);

                }


                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                    if (ClientCoreSDK.DEBUG) {
                        Log.d(TAG, "【IMCORE-netty-channelInactive】连接已断开。。。。(isLocalSocketReady=" + isLocalSocketReady()
                                + ", ClientCoreSDK.connectedToServer=" + ClientCoreSDK.getInstance().isConnectedToServer() + ")");
                    }

                    // - 20200709 add by Jack Jiang：适应用TCP协议，用于快速响应tcp连接断开事件，第一时间反馈给上层，提升用户体验
                    if (ClientCoreSDK.getInstance().isConnectedToServer()) {
                        if (ClientCoreSDK.DEBUG)
                            Log.d(TAG, "【IMCORE-netty-channelInactive】连接已断开，立即提前进入框架的“通信通道”断开处理逻辑(而不是等心跳线程探测到，那就已经比较迟了)......");

                        MBThreadPoolExecutor.runOnMainThread(() -> {
                            KeepAliveDaemon.getInstance().notifyConnectionLost();
                        });
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    super.onFailure(webSocket, t, response);
                    if (ClientCoreSDK.DEBUG)
                        Log.w(TAG, "【IMCORE-netty-exceptionCaught】异常被触发了，原因是：" + t.getMessage());
                }
            });

            if (ClientCoreSDK.DEBUG)
                Log.d(TAG, "【IMCORE-TCP】tryConnectToHost并获取connectio已完成。 .... continue ...");

            return true;
        } catch (Exception e) {
            Log.e(TAG, String.format("【IMCORE-TCP】连接Server(IP[%s])失败", ConfigEntity.serverIP), e);
            return false;
        }
    }

    public boolean isLocalSocketReady() {
        return mWebSocket != null;
    }

    public WebSocket getLocalSocket() {
        if (isLocalSocketReady()) {
            return mWebSocket;
        } else {
            return resetLocalSocket();
        }
    }

    public void closeLocalSocket() {
        this.closeLocalSocket(true);
    }

    public void closeLocalSocket(boolean silent) {
        if (ClientCoreSDK.DEBUG && !silent)
            Log.d(TAG, "【IMCORE-TCP】正在closeLocalSocket()...");

        if (this.mWebSocket != null) {
            try {
                this.mWebSocket.cancel();
                this.mWebSocket.close(1001, "客户端主动关闭连接");
                this.mWebSocket = null;
            } catch (Exception e) {
                Log.w(TAG, "【IMCORE-TCP】在closeLocalSocket方法中试图释放mWebSocket资源时：", e);
            }
        }

        if (this.client != null) {
            try {
                this.client = null;
            } catch (Exception e) {
                Log.w(TAG, "【IMCORE-TCP】在closeLocalSocket方法中试图释放client资源时：", e);
            }
        }else {
            if (!silent)
                Log.d(TAG, "【IMCORE-TCP】Socket处于未初化状态（可能是您还未登陆），无需关闭。");
        }
    }

}
