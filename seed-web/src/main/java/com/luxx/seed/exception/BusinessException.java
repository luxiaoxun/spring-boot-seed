package com.luxx.seed.exception;

import lombok.Data;

/**
 * 业务异常。不需要人工介入的叫做业务异常。
 */
@Data
public class BusinessException extends RuntimeException {
    /**
     * The encoding of the exception
     */
    private String message;
    /**
     * Exception information parameters
     */
    private Object[] args;

    public BusinessException() {
        this("common.businessError");
    }

    public BusinessException(String message) {
        this(message, null);
    }

    public BusinessException(String message, Object[] args) {
        super(message);
        this.message = message;
        this.args = args;
    }

    public BusinessException(String message, Object[] args, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.args = args;
    }

}
