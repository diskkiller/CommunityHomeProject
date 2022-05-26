package com.huaxixingfu.sqj.bean;

public class FeedBackImageBean {
    public final static int TYPE_DEFUALT  = 0 ;
    public final static int  TYPE_IMAGE_LOCAL = 1;
    public final static int  TYPE_IMAGE_HTTP = 2;

    public int defualt = TYPE_DEFUALT;
    public String imageUrl;
    public String imageUrlHttp;
    public boolean del = true;
}
