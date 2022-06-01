package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;

/**
 *    author : diskkiller
 *    desc   : 好友列表
 */
public final class FriendListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FRIEND_LIST;
    }

    public final static class Bean implements Serializable {
        public long userId;
        public long userType;
        public long chatUserId;
        public int chatFriendApplyId;
        public String userUid;
        public String userName;
        public String userNickName;
        public String userSignName;
        public String userAvatarUrl;
        public String zoneRoomAddr;
        public String chatFriendApplyRemark;

        @Override
        public String toString() {
            return "Bean{" +
                    "userId=" + userId +
                    ", userType=" + userType +
                    ", chatUserId=" + chatUserId +
                    ", chatFriendApplyId=" + chatFriendApplyId +
                    ", userUid='" + userUid + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userNickName='" + userNickName + '\'' +
                    ", userSignName='" + userSignName + '\'' +
                    ", userAvatarUrl='" + userAvatarUrl + '\'' +
                    ", zoneRoomAddr='" + zoneRoomAddr + '\'' +
                    ", chatFriendApplyRemark='" + chatFriendApplyRemark + '\'' +
                    '}';
        }
    }
}