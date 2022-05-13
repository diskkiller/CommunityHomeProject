package com.huaxixingfu.sqj.push.bean;

import java.util.List;

public class ResponseConersationList {


    public int command;
    public DataBean data;

    public static class DataBean {

        public List<Conersation> messageList;

    }
}
