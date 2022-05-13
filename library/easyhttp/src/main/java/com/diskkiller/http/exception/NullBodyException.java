package com.diskkiller.http.exception;

/**
 *    author : diskkiller
 *    time   : 2019/12/24
 *    desc   : 空实体异常
 */
public final class NullBodyException extends HttpException {

    public NullBodyException(String message) {
        super(message);
    }

    public NullBodyException(String message, Throwable cause) {
        super(message, cause);
    }
}