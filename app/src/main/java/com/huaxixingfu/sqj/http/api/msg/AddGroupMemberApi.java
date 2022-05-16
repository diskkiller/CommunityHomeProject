package com.huaxixingfu.sqj.http.api.msg;

import com.diskkiller.http.config.IRequestApi;
import com.huaxixingfu.sqj.http.api.API;

import java.util.List;

/**
 *    author : diskkiller
 *    desc   :
 */
public final class AddGroupMemberApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_FRIEND_ADD;
    }


    public final static class Bean {

        public int groupId;

        public  List<Item> chatGroupCreateUserList;

        public final static class Item {
                public  int chatToUserId;
            }

        }
}