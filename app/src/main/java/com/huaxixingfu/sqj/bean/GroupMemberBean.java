package com.huaxixingfu.sqj.bean;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMemberBean {
    public String nickname = "";
    public String userAvatarUrl;

    public String first = "";
    public int groupId;//群id
    public int userId;//用户（群成员）id
    public int userType;//账号类型1 管理平台 2 运营平台 3 社工平台 4居民
    public String residentAvatarUrl;//头像
    public String userName;//姓名
    public String userNickName;//昵称
    public String userSignName;//个性签名
    public boolean isChatGroupUserId;//当前用户是否为群主
    public boolean isFriend;//当前用户是否为好友？？？
    public boolean isSelect;//
    public boolean isAdd;//是否在群

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
