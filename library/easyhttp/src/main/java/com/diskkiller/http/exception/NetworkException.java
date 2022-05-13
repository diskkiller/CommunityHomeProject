package com.diskkiller.http.exception;

/**
 *    author : diskkiller
 *    time   : 2019/06/25
 *    desc   : 网络连接异常
 */
public final class NetworkException extends HttpException {

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}