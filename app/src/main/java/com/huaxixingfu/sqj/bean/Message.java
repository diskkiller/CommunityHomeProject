package com.huaxixingfu.sqj.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.huaxixingfu.sqj.app.AppApplication;
import com.huaxixingfu.sqj.commom.Constants;
import com.huaxixingfu.sqj.push.bean.RequestMessage;
import com.huaxixingfu.sqj.utils.SPManager;

/**
 * @Description: java类作用描述
 */
public class Message implements MultiItemEntity {

    public static final int TEXT_ME = 1;
    public static final int TEXT_YOU = 2;

    public RequestMessage msg;
    private int type = 1;//默认为自己的信息
    public boolean timeFlag;//是否展示时间戳

    @Override
    public int getItemType() {
        return type;
    }

    public Message(RequestMessage msg) {
        this.msg = msg;
        if (msg.from == SPManager.instance(AppApplication.getInstances()).getModel(Constants.USERDATA,UserData.class).userId) {
            type = TEXT_ME;
        } else {
            type = TEXT_YOU;
        }
    }

    public Message(RequestMessage msg, boolean timeFlag) {
        this.msg = msg;
        this.timeFlag = timeFlag;
        if (msg.from == 111) {
            type = TEXT_ME;
        } else {
            type = TEXT_YOU;
        }
    }

}
