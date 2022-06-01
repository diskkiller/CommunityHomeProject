package com.huaxixingfu.sqj.commom;

public class Constants {
    /**
     * sp标记
     */
    public static String SP = "sp";
    /*
     *登录成功后
     */
    public static String USERDATA = "user_data";

    /*
     *目标ID
     */
    public static String TARGETUID = "targetUid";

    /*
     *回话ID
     */
    public static String SESSIONID = "sessionId";

    /*
     *昵称
     */
    public static String NICKNAME = "nickName";

    public static final String KEY_PREF_TOKEN = "token";

    public static final String USERINFO_LOGIN = "userinfo_login";

    public static final String IS_GIUDE_SHOW = "is_guide_show";



    public static String sp_name = "killerAppSP";//sp名字";
    public static String USERINFO = "user_info";
    public static final String USERINFO_PHONE = "userinfo_phone";
    public static final String USER_ID = "user_id";

    /**
     * 默认一页数据大小 默认一页10条
     */
    int PAGE_SIZE_DEFALUT = 10;

    //TODO 正式环境需要更新

    public static String PRIVATE_KEY = "private_key";

    public static String ABOUT_KEY = "about_key";

    public static String AGREEMENT_KEY = "agreement_key";
    /**
     * 默认隐私协议-走线上-本地是预防连接网络失败
     */
    public static String PRIVACYPOLICY = "http://192.168.1.31:8086/#/privacyPolicy";

    /**
     * 默认关于我们-走线上-本地是预防连接网络失败
     */
    public static String ABOUTUS = "http://192.168.1.31:8086/#/aboutUs";

    /**
     * 默认用户协议-走线上-本地是预防连接网络失败
     */
    public static String AGREEMENT = "http://192.168.1.31:8086/#/userAgreement";

    public static String IS_AGREEMENT = "is_agreement";

    // APP版本更新相关key
    public static String UPDATE_PATH = "update_path";
    public static String UPDATE_VERSION = "update_version";
}
