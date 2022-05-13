package com.huaxixingfu.sqj.http.model;

/**
 *    desc   : 统一接口数据结构
 */
public class HttpData<T> {

    /** 返回码 */
    private String code;
    /** 提示语 */
    private String message;
    /** 数据 */
    private T data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否请求成功
     */
    public boolean isRequestSucceed() {
        return code.equals("0");
    }

    /**
     * 是否 Token 失效
     */
    public boolean isTokenFailure() {
        return code.equals("A0230");
    }
}