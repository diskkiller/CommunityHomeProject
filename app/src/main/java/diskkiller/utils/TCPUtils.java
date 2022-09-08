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
 * TCPUtils.java at 2021-7-1 15:08:17, code by Jack Jiang.
 */
package diskkiller.utils;

import android.util.Log;

import java.net.SocketAddress;

import okhttp3.WebSocket;

public class TCPUtils {
    private final static String TAG = TCPUtils.class.getSimpleName();

    public static boolean send(WebSocket skt, String putMessage) {
        return send(skt, putMessage, null);
    }

    public static synchronized boolean send(WebSocket skt, String putMessage, final MBObserver resultObserver) {
        boolean sendSucess = false;
        if ((skt != null) && (putMessage != null)) {

            // TODO: 正式发布时，关闭掉此Log，当前仅用于调试时！
//			Log.d(TAG, "【IMCORE-TCP】正在send()TCP数据时，[d.len="+d.length+",remoteIpAndPort="
//					+ TCPUtils.getSocketAdressInfo(skt.remoteAddress())+"]"
//					+ "，本地端口是："+ TCPUtils.getSocketAdressInfo(skt.localAddress())+" ...");

            try {

                boolean isSuccess =  skt.send(putMessage);
                sendSucess = true;

                if (isSuccess) {
                    Log.i(TAG, "[IMCORE-netty-send异步回调] >> 数据已成功发出[dataLen=" + putMessage + "].");
                }
                else {
                    Log.w(TAG, "[IMCORE-netty-send异步回调] >> 数据发送失败！[dataLen=" + putMessage + "].");
                }

                if (resultObserver != null)
                    resultObserver.update(isSuccess, null);



                return sendSucess;
            } catch (Exception e) {

                Log.e(TAG, "【IMCORE-TCP】send方法中》》发送TCP数据报文时出错了，原因是：" + e.getMessage(), e);
                if (resultObserver != null)
                    resultObserver.update(false, null);
            }
        } else {
            Log.w(TAG, "【IMCORE-TCP】send方法中》》无效的参数：skt==null || putMessage == null!");
        }

        if (resultObserver != null)
            resultObserver.update(false, null);

        return sendSucess;
    }

    public static String getSocketAdressInfo(SocketAddress s) {
        return (s != null) ? s.toString() : null;
    }
}
