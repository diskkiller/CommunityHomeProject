package com.huaxixingfu.sqj.bean;

import java.util.List;

public class ZoneResidentBean {

    public long userPrecinctId;
    public long userPrecinctSocpeId;
    public int userPrecinctType;
    public String name;
    public List<ZoneResidentBean> children;


    /**
     * description:
     * 居民信息
     *
     * userId	integer($int64)
     * title: 用户id
     * residentName	string
     * title: 姓名
     * nickName	string
     * title: 昵称
     * residentPhoneNum	string
     * title: 联系电话
     * avatar	string
     * title: 头像
     * zoneRoomAddr	string
     * title: 住址
     * isFriend	boolean
     * title: 是否好友
     */
    public int userId;
    public String residentName;
    public String nickName;
    public String residentPhoneNum;
    public String avatar;
    public String zoneRoomAddr;
    public boolean isFriend;

}
