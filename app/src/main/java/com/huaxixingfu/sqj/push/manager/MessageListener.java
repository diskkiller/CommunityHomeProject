package com.huaxixingfu.sqj.push.manager;

import okio.ByteString;

public class MessageListener {

    /**
     * 被观察者接口定义
     */
    public interface MessageObserverable {

        void register(MessageObserver messageObserver);

        void remove(MessageObserver messageObserver);

        void message(String text);

        void message(ByteString bytes);
    }

    /**
     * 观察者接口定义
     */
    public interface MessageObserver {

        void onNewMessage(String text);

        void onNewMessage(ByteString bytes);

    }
}
