package com.huaxixingfu.sqj.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class HomeCenterBean {
    public int sysBarAccessDayNum;
    public int sysBarAccessTotalNum;
    public List<ChatRoomVOList> chatRoomVOList;



    public class ChatRoomVOList implements Serializable {

        public int chatRoomUserId;
        public int zoneId;
        public String zoneName;
        public int chatGroupId;
        public int sysBarId;
        public String chatRoomName;
        public String chatRoomAvatar;
        public String chatRoomIntroduce;
        public int chatRoomCheckFlag;
        public int chatRoomPersonNum;
        public int chatRoomSort;
        public Date createdAt;

    }
}
