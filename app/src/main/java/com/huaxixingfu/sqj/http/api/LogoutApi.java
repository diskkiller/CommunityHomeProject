package com.huaxixingfu.sqj.http.api;

import com.diskkiller.http.config.IRequestApi;

/**
 *    author : diskkiller
 *    desc   : 退出登录
 */
public final class LogoutApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/logout";
    }
}