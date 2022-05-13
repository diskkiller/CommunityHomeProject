package com.huaxixingfu.sqj.push.bean;

public class RequestMessage {

    public long from;
    public long to;
    public int cmd;
    public long createTime;
    public int msgType;
    public int chatType;
    public String groupId;
    public String content;
    public ExtrasDTO extras;
    public String id;
    public long systemTime;

    public static class ExtrasDTO {
        public String a;
        public String nickName;
        public String avatarUrl;
    }
}
