package com.huaxixingfu.sqj.bean;

import java.util.Date;
import java.util.List;

public class GroupDetailBean {
    private int chatGroupId;
    private int chatUserId;
    private int chatGroupUserId;
    private String chatGroupNiceName;
    private String chatGroupIntroduce;
    private Date chatGroupIntroduceModifiedAt;
    private String chatGroupNoticeContent;
    private boolean isChatGroupUserId;
    private String chatUserNiceName;
    private int chatTopFlag;
    private List<String> userAvatarUrl;
    public void setChatGroupId(int chatGroupId) {
        this.chatGroupId = chatGroupId;
    }
    public int getChatGroupId() {
        return chatGroupId;
    }

    public void setChatUserId(int chatUserId) {
        this.chatUserId = chatUserId;
    }
    public int getChatUserId() {
        return chatUserId;
    }

    public void setChatGroupUserId(int chatGroupUserId) {
        this.chatGroupUserId = chatGroupUserId;
    }
    public int getChatGroupUserId() {
        return chatGroupUserId;
    }

    public void setChatGroupNiceName(String chatGroupNiceName) {
        this.chatGroupNiceName = chatGroupNiceName;
    }
    public String getChatGroupNiceName() {
        return chatGroupNiceName;
    }

    public void setChatGroupIntroduce(String chatGroupIntroduce) {
        this.chatGroupIntroduce = chatGroupIntroduce;
    }
    public String getChatGroupIntroduce() {
        return chatGroupIntroduce;
    }

    public void setChatGroupIntroduceModifiedAt(Date chatGroupIntroduceModifiedAt) {
        this.chatGroupIntroduceModifiedAt = chatGroupIntroduceModifiedAt;
    }
    public Date getChatGroupIntroduceModifiedAt() {
        return chatGroupIntroduceModifiedAt;
    }

    public void setChatGroupNoticeContent(String chatGroupNoticeContent) {
        this.chatGroupNoticeContent = chatGroupNoticeContent;
    }
    public String getChatGroupNoticeContent() {
        return chatGroupNoticeContent;
    }

    public void setIsChatGroupUserId(boolean isChatGroupUserId) {
        this.isChatGroupUserId = isChatGroupUserId;
    }
    public boolean getIsChatGroupUserId() {
        return isChatGroupUserId;
    }

    public void setChatUserNiceName(String chatUserNiceName) {
        this.chatUserNiceName = chatUserNiceName;
    }
    public String getChatUserNiceName() {
        return chatUserNiceName;
    }

    public void setChatTopFlag(int chatTopFlag) {
        this.chatTopFlag = chatTopFlag;
    }
    public int getChatTopFlag() {
        return chatTopFlag;
    }

    public void setUserAvatarUrl(List<String> userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }
    public List<String> getUserAvatarUrl() {
        return userAvatarUrl;
    }
}
