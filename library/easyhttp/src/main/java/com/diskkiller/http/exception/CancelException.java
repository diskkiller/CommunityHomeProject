package com.diskkiller.http.exception;

/**
 *    author : diskkiller
 *    time   : 2019/06/25
 *    desc   : 请求取消异常
 */
public final class CancelException extends HttpException {

    public CancelException(String message) {
        super(message);
    }

    public CancelException(String message, Throwable cause) {
        super(message, cause);
    }
}