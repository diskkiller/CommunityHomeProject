package com.huaxixingfu.sqj.push.bean;

import java.util.List;

public class ResponseMessageRecord {

    public int command;
    public DataDTO data;

    public static class DataDTO {
        public List<RequestMessage> bodies;
    }
}
