package com.huaxixingfu.sqj.http.api.msg;

import com.diskkiller.http.config.IRequestApi;
import com.huaxixingfu.sqj.http.api.API;
import com.huaxixingfu.sqj.http.api.VerifyCodeApi;

import java.io.Serializable;
import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 创建群组
 */
public final class CreatGroutApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.CHAT_GROUP_CREAT;
    }


    public final static class Bean {

        public  List<Item> chatGroupCreateUserList;

        public final static class Item {
                public  int chatToUserId;
            }

        }
}