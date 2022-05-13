package com.huaxixingfu.sqj.utils;

import java.util.regex.Pattern;

public class MatchUtils {
    /**密码校验*/
    public static final String PASSWORD_PRINCIPLE = "^(a-z|A-Z|0-9)*[^$%^&*;:,<>?()\\\"\"\\']{6,8}$";

    public static boolean isRightPwd(String pwd) {
        return Pattern.compile(PASSWORD_PRINCIPLE).matcher(pwd).matches();
    }
}
