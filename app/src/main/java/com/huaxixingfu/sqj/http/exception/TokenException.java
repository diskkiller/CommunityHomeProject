package com.huaxixingfu.sqj.http.exception;


import com.diskkiller.http.exception.HttpException;

/**
 *    time   : 2019/05/19
 *    desc   : Token 失效异常
 */
public final class TokenException extends HttpException {

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}