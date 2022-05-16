package com.huaxixingfu.sqj.http.api;

import com.huaxixingfu.sqj.BuildConfig;

public class API {

    /*
     *测试服务器
     */
    public static String MAIN_URL = "";


    public static final String LOGIN_BY_CODE = MAIN_URL + "sggl/mobile/user/v1.0/login/submit";
    /**
     * 注册
     */
    public static final String REGISTER = MAIN_URL + "sggl/mobile/user/v1.0/register/submit";
    /**
     * 校验注册手机号
     */
    public static final String REGISTER_VALIDATE_PHONE = MAIN_URL +"sggl/mobile/user/v1.0/register/validate";
    /**
     * 获取注册验证码
     * */
    public static final String CODE_REGISTER = MAIN_URL  +"sggl/mobile/user/v1.0/register/code";

    /**
     * 校验登录手机号
     */
    public static final String LOGIN_VALIDATE_PHONE = MAIN_URL +"sggl/mobile/user/v1.0/login/validate";
    /**
     * 密码登录
     */
    public static final String LOGIN_BY_PWD = MAIN_URL + "sggl/mobile/user/v1.0/login/password/submit";
    /**
     * 获取登录验证码
     */
    public static final String LOGIN_GET_CODE = MAIN_URL + "sggl/mobile/user/v1.0/login/code";
    /**
     * 获取修改密码的验证码
     */
    public static final String FORGET_GET_CODE = MAIN_URL + "sggl/mobile/user/v1.0/reset/code";
    /**
     * 修改密码
     */
    public static final String FORGET_RESET_PWD = MAIN_URL+  "sggl/mobile/user/v1.0/reset/submit";
    /**
     * 通讯录
     */
    public static final String GET_MAILLIST = MAIN_URL+  "sggl/mobile/chat/friend/v1.0/friend/list";

    /**
     * 申请好友数量
     */
    public static final String FRIEND_APPLY_COUNT = MAIN_URL+  "sggl/mobile/chat/friend/v1.0/apply/count";



    /**
     * 用户反馈意见
     */
    public static final String FEEDBACK_CONTENT = MAIN_URL +"sggl/mobile/mycenter/v1.0/feedback/submit";
    /**
     * 获取反馈问题类型
     */
    public static final String FEEDBACK_TYPE = MAIN_URL +"sggl/mobile/mycenter/v1.0/feedback/init/type";

    public static final String LOGIN = MAIN_URL + "";

    /**
     * 轮播
     */
    public static final String BANNER = MAIN_URL + "sggl/mobile/home/v1.0/banner/list";

    /**
     * 首页-新闻
     */
    public static final String HOME_CONTENT_NEW = MAIN_URL + "sggl/mobile/home/v1.0/news/top/page";

    /**
     * 首页-新闻-分类
     */
    public static final String NEWS_COLUMN = MAIN_URL + "sggl/mobile/home/v1.0/news/init/column";

    /**
     * 首页-新闻-分类
     */
    public static final String NEWS_PAGE = MAIN_URL + "sggl/mobile/home/v1.0/news/page";

    /**
     * 首页-公告列表
     */
    public static final String NOTICE_LIST = MAIN_URL + "sggl/mobile/home/v1.0/notice/list";

    /**
     * 系统公告列表
     */
    public static final String SYSTEM_NOTIES_LIST = MAIN_URL + "sggl/mobile/home/v1.0/notice/page";


    /**
     * 居民认证-初始化
     */
    public static final String RESIDENT_INIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/resident/init";

    /**
     * 居民认证-社区居民信息
     */
    public static final String RESIDENT_COMMUNITY = MAIN_URL + "sggl/mobile/mycenter/v1.0/resident/community";

    /**
     * 居民认证-社区居民信息-提交
     */
    public static final String RESIDENT_SUBMIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/resident/submit";

    /**
     * 查询好友
     */
    public static final String FRIEND_QUERY = MAIN_URL + "sggl/mobile/chat/friend/v1.0/apply/query";

    /**
     * 同意好友
     */
    public static final String APPROVE_FRIEND = MAIN_URL + "sggl/mobile/chat/friend/v1.0/apply/approve";

    /**
     * 拒绝好友
     */
    public static final String REFUSE_FRIEND = MAIN_URL + "sggl/mobile/chat/friend/v1.0/apply/refuse";

    /**
     * 申请好友
     */
    public static final String SUBMIT_FRIEND = MAIN_URL + "sggl/mobile/chat/friend/v1.0/apply/submit";

    /**
     * 申请好友列表
     */
    public static final String FRIEND_LIST = MAIN_URL + "sggl/mobile/chat/friend/v1.0/apply/list";



    /*=============我的===============*/




    /**
     * 修改手机号，获取验证码
     */
    public static final String ME_SECURITY_CODE = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/code";
    /**
     * 修改密码，获取验证码
     */
    public static final String ME_SECURITY_PASSWORD_CODE = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/password/code";
    /**
     * 修改手机号，提交
     */
    public static final String ME_SECURITY_SUBMIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/submit";
    /**
     * 修改密码，提交
     */
    public static final String ME_SECURITY_PASSWORD_SUBMIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/password/submit";
    /**
     * 家庭成员信息列表
     */
    public static final String ME_FAMILY_INIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/family/init";
    /**
     * 个人信息初始化
     */
    public static final String ME_PERSON_INIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/init";
    /**
     * 实名认证初始化
     */
    public static final String ME_PERSON_REALNAME_INIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/realname/init";
    /**
     * 查询性别
     */
    public static final String ME_PERSON_QRY_SEX = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/qry/sex/enums";
    /**
     * 查询政治面貌
     */
    public static final String ME_PERSON_QRY_POLITICS = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/qry/politics";
    /**
     * 查询民族
     */
    public static final String ME_PERSON_QRY_NATION = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/qry/nation";
    /**
     * 更新性别
     */
    public static final String ME_PERSON_EDIT_SEX = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/sex";
    /**
     * 上传图片/文件
     */
    public static final String COMMON_IMAGE_UPLOAD = MAIN_URL + "sggl/mobile/file/v1.0/image/upload";
    /**
     * 更新昵称
     */
    public static final String ME_PERSON_EDIT_NICKNAME = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/nickname";
    /**
     * 更新头像
     */
    public static final String ME_PERSON_EDIT_HEAD = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/head";
    /**
     * 更新民族
     */
    public static final String ME_PERSON_EDIT_NATION = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/nation";
    /**
     * 个性签名
     */
    public static final String ME_PERSON_EDIT_INTRODUCE = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/introduce";
    /**
     * 政治面貌
     */
    public static final String ME_PERSON_EDIT_POLITICS = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/politics";

    /**
     * 隐私政策
     */
    public static final String PRIVACY_DETAIL = MAIN_URL + "sggl/mobile/home/v1.0/guide/privacy/detail";

    /**
     * 用户协议
     */
    public static final String AGREEMENT_URL = MAIN_URL + "sggl/mobile/home/v1.0/guide/user/agreement/detail";


    /**
     * 关于我们
     */
    public static final String ABOUT_DETAIL = MAIN_URL + "sggl/mobile/home/v1.0/guide/about/us/detail";

    /**
     * 职业
     */
    public static final String ME_PERSON_EDIT_OCCUPATION = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/occupation";
    /**
     * 生日
     */
    public static final String ME_PERSON_EDIT_BIR = MAIN_URL + "sggl/mobile/mycenter/v1.0/person/edit/bir";
    /**
     * 实名认真提交
     */
    public static final String ME_PERSON_REALNAME_SUBMIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/realname/submit";

    /**
     * 注销获取验证码
     */
    public static final String ME_SECURITY_LOGOUT_CODE = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/logout/code";
    /**
     * 确认注销
     */
    public static final String ME_SECURITY_LOGOUT_SUBMIT = MAIN_URL + "sggl/mobile/mycenter/v1.0/security/logout/submit";


    //===================================================================
    /**
     * 社工列表
     */
    public static final String MSG_RESIDENT_LIST = MAIN_URL + "sggl/mobile/chat/friend/v1.0/resident/list";

    /**
     * 通知公告
     */
    public static final String HOME_NOTICE_MESSAGEPAGE = MAIN_URL + "sggl/mobile/home/v1.0/notice/message/page";

    /**
     * 通知公告-状态更新
     */
    public static final String HOME_NOTICE_MESSAGEPAGE_EDITE = MAIN_URL + "sggl/mobile/home/v1.0/notice/message/status";


    /**
     * 任务消息
     */
    public static final String HOME_NOTICE_MESSAGEPAGE_TASK = MAIN_URL + "sggl/mobile/meassge/v1.0/message/task";

    /**
     * 任务消息-状态更新
     */
    public static final String HOME_NOTICE_MESSAGEPAGE_TASK_READ = MAIN_URL + "sggl/mobile/meassge/v1.0/message/task/read";

    /**
     * 修改好友备注
     */
    public static final String CHAT_FRIEND_EDITE_RENAME = MAIN_URL + "sggl/mobile/chat/friend/v1.0/update/nice";

    /**
     * 创建群组
     */
    public static final String CHAT_GROUP_CREAT = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/create";

    /**
     * 我加入的群组
     */
    public static final String CHAT_GROUP_JOIN_LIST = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/list";
    /**
     * 我创建的群组
     */
    public static final String CHAT_GROUP_CREAT_LIST = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/create/list";
    /**
     * 邀请我的群组
     */
    public static final String CHAT_GROUP_APPLY_LIST = MAIN_URL + "sggl/mobile/chat/group/v1.0/apply/list";

    /**
     * 我的群组详情
     */
    public static final String CHAT_GROUP_DETAIL = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/detail";

    /**
     * 群公告列表
     */
    public static final String CHAT_GROUP_NOTICE = MAIN_URL + "sggl/mobile/chat/group/v1.0/notice/list";

    /**
     * 群公告增加
     */
    public static final String CHAT_GROUP_NOTICE_ADD = MAIN_URL + "sggl/mobile/chat/group/v1.0/notice/add";

    /**
     * 邀请好友加群
     */
    public static final String CHAT_GROUP_FRIEND_ADD = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/friend/add";

    /**
     * 群好友列表
     */
    public static final String CHAT_GROUP_FRIEND_LIST = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/friend/list";

    /**
     * 删除群好友
     */
    public static final String CHAT_GROUP_FRIEND_DEL = MAIN_URL + "sggl/mobile/chat/group/v1.0/group/friend/del";





}
