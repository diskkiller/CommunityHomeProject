package com.huaxixingfu.sqj.bean;

public class FriendDetailBean {

    public int chatUserId;
    public int userId;//用户（群成员）id
    public int userType;//账号类型1 管理平台 2 运营平台 3 社工平台 4居民
    public int chatTopFlag;//置顶
    public String userAvatarUrl;//头像
    public String userName;//好友姓名      (如果是 社工平台 昵称显示姓名，备注昵称不显示，地址不显示，昵称不可编辑)
    public String userNickName;//好友昵称
    public String userSignName;//个性签名
    public String chatFriendNiceName;//好友备注昵称（由好友所有者设置）
    public String zoneRoomAddr;//好友住址
    public String chatFriendApplyRemark;//好友申请信息

}
