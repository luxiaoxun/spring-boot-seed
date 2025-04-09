package com.luxx.seed.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private static final long serialVersionUID = -1068765335343416833L;

    private final String code;

    public AuthException() {
        super("没有操作权限");
        this.code = String.valueOf(HttpStatus.UNAUTHORIZED.value());
    }

    public AuthException(String message) {
        super(message);
        this.code = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }
}
