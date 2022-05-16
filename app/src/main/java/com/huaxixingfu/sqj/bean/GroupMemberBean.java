package com.huaxixingfu.sqj.bean;

public class GroupMemberBean {
    public String nickname = "";
    public String userAvatarUrl;


    public int groupId;//群id
    public int userId;//用户（群成员）id
    public int userType;//账号类型1 管理平台 2 运营平台 3 社工平台 4居民
    public String residentAvatarUrl;//头像
    public String userName;//姓名
    public String userNickName;//昵称
    public String userSignName;//个性签名
    public boolean isChatGroupUserId;//当前用户是否为群主
    public boolean isFriend;//当前用户是否为好友？？？
}
