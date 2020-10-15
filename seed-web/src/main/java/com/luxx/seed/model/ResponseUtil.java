package com.luxx.seed.model;

import com.luxx.seed.constant.ErrorCode;

public class ResponseUtil {
    public ResponseUtil() {
    }

    public static Response success() {
        return generateResult(ErrorCode.SUCCESS, (Object) null);
    }

    public static Response success(Object data) {
        return generateResult(ErrorCode.SUCCESS, data);
    }

    public static Response fail() {
        return generateResult(ErrorCode.SYSTEM_ERROR, (Object) null);
    }

    public static Response fail(String code, String msg) {
        return generateResult(code, msg, (Object) null);
    }

    public static Response fail(ErrorCode errorCode) {
        return generateResult(errorCode.getCode(), errorCode.getMsg(), (Object) null);
    }

    public static Response generateResult(String code, String msg, Object data) {
        return new Response(code, msg, data);
    }

    public static Response generateResult(ErrorCode errorCode, Object data) {
        return generateResult(errorCode.getCode(), errorCode.getMsg(), data);
    }
}
