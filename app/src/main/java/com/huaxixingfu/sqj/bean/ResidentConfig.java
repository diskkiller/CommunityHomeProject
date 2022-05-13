package com.huaxixingfu.sqj.bean;

/**
 * 作者：lblbh on 2022/5/1 18:27
 * 邮箱：lanbuhan@163.com
 * 功能：居民认证配置信息
 */

public interface ResidentConfig {

    //TODO
    //下面的对应数值不能需改-->请求需要
    //    地址等级(3-社区 4-小区 5-楼 6-单元 7-层 8-房间)
    int level_community = 3;
    int level_quarters = 4;
    int level_number = 5;
    int level_unit = 6;
    int level_floor = 7;
    int level_room = 8;
    int level_normal = 0;
}
