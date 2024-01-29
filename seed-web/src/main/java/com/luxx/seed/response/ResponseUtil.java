package com.luxx.seed.response;

import com.luxx.seed.config.i18n.I18nMessageUtil;

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

    public static Response fail(ResponseCode responseCode) {
        return generateResult(responseCode.getCode(), I18nMessageUtil.getMsg(responseCode.getMsg()), (Object) null);
    }

    public static Response generateResult(String code, String msg, Object data) {
        return new Response(code, msg, data);
    }

    public static Response generateResult(ResponseCode responseCode, Object data) {
        return generateResult(responseCode.getCode(), I18nMessageUtil.getMsg(responseCode.getMsg()), data);
    }
}
