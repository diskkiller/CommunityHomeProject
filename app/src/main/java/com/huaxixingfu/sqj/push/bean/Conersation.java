package com.huaxixingfu.sqj.push.bean;

public class Conersation {
    public boolean isPinned;
    public String avatarUrl;
    public RequestMessage chatBody;
    public int chatTopFlag;
    public String nickName;
    public String sessionId;//"n-1":通知公告  "n-2":任务消息
    public int unreadMsgNum;
}
