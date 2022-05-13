package com.huaxixingfu.sqj.push.bean;

public class RequestHeartBeat {

    public RequestHeartBeat(int cmd, int hbbyte) {
        this.cmd = cmd;
        this.hbbyte = hbbyte;
    }

    public int cmd;
    public int hbbyte;
}
