package com.diskkiller.http.exception;

/**
 *    author : diskkiller
 *    time   : 2019/06/25
 *    desc   : 服务器超时异常
 */
public final class TimeoutException extends HttpException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}