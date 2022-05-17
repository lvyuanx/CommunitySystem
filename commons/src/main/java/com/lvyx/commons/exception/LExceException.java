package com.lvyx.commons.exception;

/**
 * <p>
 * 自定义社区异常异常
 * </p>
 *
 * @author lvyx
 * @since 2022-05-01 17:39:20
 */
public class LExceException extends Exception {

    public LExceException() {
        super();
    }

    public LExceException(String message) {
        super(message);
    }

    public LExceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LExceException(Throwable cause) {
        super(cause);
    }

    protected LExceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
