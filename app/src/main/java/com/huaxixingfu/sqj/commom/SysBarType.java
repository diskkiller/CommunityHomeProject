package com.huaxixingfu.sqj.commom;

/**
 * Created by lsm on 2022/5/28.
 * Describe:
 */
public class SysBarType {


    /**
     * sys_bar_type = 3 调整新闻 ，取 sys_bar_code 值进行调用新闻页面接口进行调整 ；
     * sys_bar_type = 1 外部链接   取 sys_bar_url  值，直接调整；
     *
     * sys_bar_type = 0  本系统调整 区 sys_bar_code 进行本地业务判断，然后进行调整  ；（办事大厅： BANSHIDATING   社工在线： SHEGONGZAIXIAN ） 防疫动态： YIQINGFANGKONG
     *
     * sys_bar_type = 0  本系统调整 区 sys_bar_url 进行本地业务判断，然后进行调整  ；（办事大厅： CommunityHome://home-officehall   社工在线： CommunityHome://home-officehall ） 防疫动态： CommunityHome://home-epidemicdynamic
     *
     *
     * 调整类型： sys_bar_type 类型：
     * THIS_SYS (0, "本系统"),
     * EXTERNAL_LINKS(1, "外部链接"),
     * APP(2, "app应用"),  //没有使用
     * NEWS(3, "新闻"),
     * NOTICE(4, "公告"),  //没有使用
     * FUNCTIONS_TO_DEVELOPED(5, "待开发功能"),
     * PAGE_TO_DEVELOPED(6, "待开发页面"),
     */

    public static final  int NATIVE = 0;
    public static final  int H5 = 1;
    public static final  int NATIVE_APP = 2;
    public static final  int NEWS = 3;
    public static final  int NOTICE = 4;
    public static final  int FUNCTIONS_TO_DEVELOPED = 5;
    public static final  int PAGE_TO_DEVELOPED = 6;

}
