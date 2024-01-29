package com.luxx.engine.response;

public class ResponseUtil {
    public ResponseUtil() {
    }

    public static Response success() {
        return generateResult(ResponseCode.SUCCESS, (Object) null);
    }

    public static Response success(Object data) {
        return generateResult(ResponseCode.SUCCESS, data);
    }

    public static Response fail() {
        return generateResult(ResponseCode.SYSTEM_ERROR, (Object) null);
    }

    public static Response fail(String code, String msg) {
        return generateResult(code, msg, (Object) null);
    }

    public static Response fail(ResponseCode errorCode) {
        return generateResult(errorCode.getCode(), errorCode.getMsg(), (Object) null);
    }

    public static Response generateResult(String code, String msg, Object data) {
        return new Response(code, msg, data);
    }

    public static Response generateResult(ResponseCode errorCode, Object data) {
        return generateResult(errorCode.getCode(), errorCode.getMsg(), data);
    }
}
