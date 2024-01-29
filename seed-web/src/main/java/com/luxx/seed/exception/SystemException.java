package com.luxx.seed.exception;

import lombok.Data;

/**
 * 系统异常。需要人工介入的异常叫做系统异常。
 */
@Data
public class SystemException extends RuntimeException {

    /**
     * The encoding of the exception
     */
    private String message;
    /**
     * Exception information parameters
     */
    private Object[] args;

    public SystemException() {
        this("common.systemError");
    }

    public SystemException(String message) {
        this(message, null);
    }

    public SystemException(String message, Object[] args) {
        super(message);
        this.message = message;
        this.args = args;
    }

    public SystemException(String message, Object[] args, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.args = args;
    }
}
