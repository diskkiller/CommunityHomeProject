package diskkiller.msgBean;

import java.util.UUID;

public class Message {

    public String from;
    public String to;
    public int cmd;
    public long createTime;
    public int msgType;
    public int chatType;
    public String groupId;
    public String content;
    public ExtrasDTO extras;
    public String id;
    public long systemTime;
    protected int code = 0;
    protected String dataContent;

    protected String fp;

    protected boolean QoS;
    protected transient int retryCount;

    public Message() {

    }

    public Message(int type, String dataContent, String from, String to) {
        this(type, dataContent, from, to, -1);
    }

    public Message(int type, String dataContent, String from, String to, int typeu) {
        this(type, dataContent, from, to, false, (String)null, typeu);
    }

    public Message(int type, String dataContent, String from, String to, boolean QoS, String fingerPrint) {
        this(type, dataContent, from, to, QoS, fingerPrint, -1);
    }

    public Message(int type, String dataContent, String from, String to, boolean QoS, String fingerPrint, int typeu) {
        this.dataContent = null;
        this.from = "-1";
        this.to = "-1";
        this.fp = null;
        this.QoS = false;
        this.retryCount = 0;
        this.chatType = type;
        this.dataContent = dataContent;
        this.from = from;
        this.to = to;
        this.QoS = QoS;
        if (QoS && fingerPrint == null) {
            this.fp = genFingerPrint();
        } else {
            this.fp = fingerPrint;
        }

    }

    public static class ExtrasDTO {
        public String a;
        public String nickName;
        public String avatarUrl;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }

    public boolean isQoS() {
        return QoS;
    }

    public void setQoS(boolean qoS) {
        QoS = qoS;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void increaseRetryCount() {
        ++this.retryCount;
    }

    public Object clone() {
        Message cloneMsg = new Message(this.getChatType(), this.getDataContent(), this.getFrom(), this.getTo(), this.isQoS(), this.getFp());
        return cloneMsg;
    }

    public static long genServerTimestamp() {
        return System.currentTimeMillis();
    }

    public static String genFingerPrint() {
        return UUID.randomUUID().toString();
    }
}
