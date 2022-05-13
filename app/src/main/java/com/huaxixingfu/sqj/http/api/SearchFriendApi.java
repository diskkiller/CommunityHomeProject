package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;
import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 查找用户
 */
public final class SearchFriendApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.FRIEND_QUERY;
    }

    public final static class Bean {
        public long userId;
        public int userType;
        public String userUid;
        public String userName;
        public String userNickName;
        public String userSignName;
        public String userAvatarUrl;
        public String zoneRoomAddr;
        public List<VFriendItem> tchatResidentRoomVoList;

        public final static class VFriendItem implements Serializable {

            public long userId;
            public long zoneRoomId;
            public String zoneRoomAddr;

        }
    }
}