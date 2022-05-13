package com.huaxixingfu.sqj.push.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.huaxixingfu.sqj.BuildConfig;
import com.huaxixingfu.sqj.R;
import com.huaxixingfu.sqj.bean.UserData;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.push.bean.ResponseMessage;
import com.huaxixingfu.sqj.push.manager.MessageListener;
import com.huaxixingfu.sqj.push.manager.WebSocketManager;
import com.huaxixingfu.sqj.ui.activity.HomeActivity;
import com.huaxixingfu.sqj.utils.LogUtil;
import com.huaxixingfu.sqj.utils.SPManager;

import okio.ByteString;


public class ImService extends Service {

    private Vibrator vibrator;
    private MessageListener.MessageObserver messageObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initIM();
        LogUtil.e("aaaaaaaImService", "已创建");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID;
            String channelName = BuildConfig.APPLICATION_ID + ".ImService";
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.icon_logo)  // the status icon
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentText("IM服务正在运行")  // the contents of the entry
                    .build();

            startForeground(2, notification);
        }
    }

    private void initIM() {

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        messageObserver = new MessageListener.MessageObserver() {

            @Override
            public void onNewMessage(String text) {
                LogUtil.e("aaaaaaImService", "新消息-----" + text);


//                        取消上一条未播放完的语音
//                        if (AudioPlayer.getInstance().isPlaying()) {
//                            AudioPlayer.getInstance().stopPlay();
//                            vibrator.cancel();
//                        }
//                        //播放声音
//                        AudioPlayer.getInstance().startPlay(ImService.this, R.raw.ding, false);
//                        vibrator.vibrate(300);

                int command = WebSocketManager.getCommand(text);
                if (command == WebSocketManager.COMMAND_CHAT_REQ) {
                    ResponseMessage json = new Gson().fromJson(text, ResponseMessage.class);
                    if (json.data.from != SPManager.instance(getApplicationContext()).getModel(Constants.USERDATA, UserData.class).userId && !TextUtils.isEmpty(json.data.content)) {
                        CreateNoti(json.data.content);
                    }
                }
            }

            @Override
            public void onNewMessage(ByteString bytes) {

            }
        };

        WebSocketManager.getInstance().register(messageObserver);

    }

    /**
     * 创建一个通知
     *
     * @param text
     */
    public void CreateNoti(String text) {

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //适配 Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建 NotificationChannel 对象
            NotificationChannel channel = new NotificationChannel("9527",
                    "chat message", NotificationManager.IMPORTANCE_DEFAULT);
            //创建通知渠道
            manager.createNotificationChannel(channel);
        }
        //点击后打开主页即可
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "9527")
                .setContentTitle("新消息")
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icon_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo));

        mBuilder.setContentIntent(pendingIntent);
        //发送通知( id唯一,可用于更新通知时对应旧通知; 通过mBuilder.build()拿到notification对象 )
        manager.notify(9527, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().remove(messageObserver);
    }

}
