package com.huaxixingfu.sqj.http.api;

import android.text.TextUtils;

import com.diskkiller.http.config.IRequestApi;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *    author : diskkiller
 *    desc   : 通信录
 */
public final class MailListApi implements IRequestApi {

    @Override
    public String getApi() {
        return API.GET_MAILLIST;
    }

    /**
     * chatFriendId	integer($int64)
     * title: id
     * chatUserId	integer($int64)
     * title: 用户id
     * chatFriendUserType	integer($int32)
     * title: 用户类型
     * chatToUserId	integer($int64)
     * title: 申请人id
     * chatFriendNiceName	string
     * title: 好友昵称
     * userName	string
     * title: 姓名
     * userNickName	string
     * title: 昵称
     * chatFriendRemark	string
     * title: 好友备注
     * userAvatarUrl	string
     * title: 头像
     * zoneRoomAddr	string
     * title: 住址
     * flag	boolean
     * title: 是否在群
     */
    public final static class Bean implements Serializable {
        public String first = "";
        public int chatFriendId;
        public int chatUserId;
        public int chatFriendUserType;
        public int chatToUserId;
        public String chatFriendNiceName;
        public String userNickName;
        public String userName;
        public String chatFriendRemark;
        public String userAvatarUrl;
        public String zoneRoomAddr;
        public boolean flag;
        public boolean isAdd;//是否在群

        public boolean isSelect;

        /***
         * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
         * @return
         */
        public String getSection() {
            if (first.equals("header")) {
                return "header";
            } else if (TextUtils.isEmpty(first)) {
                return "#";
            } else {
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(first);
                if (m.matches()) {
                    return first.toUpperCase();
                } else
                    return "#";
            }
        }
    }
}