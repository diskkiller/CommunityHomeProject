package com.diskkiller.http.exception;

/**
 *    author : diskkiller
 *    time   : 2019/06/25
 *    desc   : 数据解析异常
 */
public final class DataException extends HttpException {

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}