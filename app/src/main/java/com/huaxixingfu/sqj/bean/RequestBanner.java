package com.huaxixingfu.sqj.bean;
;
import com.huaxixingfu.sqj.bean.VBanner;

import java.util.List;

/**
 * 作者：lblbh on 2022/4/30 15:36
 * 邮箱：lanbuhan@163.com
 * 功能：请求轮播
 */

public class RequestBanner {

    public List<VBanner> data;

    public List<VBanner> getData() {
        return data;
    }

    public void setData(List<VBanner> data) {
        this.data = data;
    }
}
