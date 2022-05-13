package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;
import java.util.List;

/**
 *    author : diskkiller
 *    desc   : 获取家庭信息
 */
public final class FamilyDataApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.ME_FAMILY_INIT;
    }

    public final static class Bean{
        public List<VFamily> list;

        public String address;


        public class VFamily {

            /**
             "userName": "string",
             "userAvatarUrl": "string",
             "phoneNum": "string",
             "flag": true,
             "flagName": "string"
             */
            public String userName;
            public String phoneNum;
            public String flagName;
            public String userAvatarUrl;
            public boolean flag;


        }
    }
}