package com.huaxixingfu.sqj.push.bean;

public class RequestMessage {

    public long from;
    public long to;
    public int cmd;
    public long createTime;
    public int msgType;
    public int chatType;//1群  2私
    public long groupId;//后改的 类型不对  服了
    public String content;
    public String sessionId;//没有 后加的 服了。。。
    public ExtrasDTO extras;
    public String id;
    public long systemTime;

    public static class ExtrasDTO {
        public String a;
        public String nickName;
        public String avatarUrl;
    }
}
