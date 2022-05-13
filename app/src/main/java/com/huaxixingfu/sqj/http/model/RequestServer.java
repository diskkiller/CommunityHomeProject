package com.huaxixingfu.sqj.http.model;

import androidx.annotation.NonNull;

import com.diskkiller.http.config.IRequestApi;
import com.huaxixingfu.sqj.other.AppConfig;
import com.diskkiller.http.config.IRequestServer;
import com.diskkiller.http.model.BodyType;

/**
 *    desc   : 服务器配置
 */
public class RequestServer implements IRequestServer,IRequestApi {

    @Override
    public String getHost() {
        return AppConfig.getHostUrl();
    }

    @Override
    public String getApi() {
        return "api/";
    }

    @NonNull
    @Override
    public BodyType getBodyType() {
        // 以表单的形式提交参数
        return BodyType.FORM;
    }
}