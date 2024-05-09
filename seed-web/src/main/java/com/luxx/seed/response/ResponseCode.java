package com.luxx.seed.response;

public enum ResponseCode {
    SUCCESS("200", "common.success"),
    SYSTEM_ERROR("10001", "common.fail"),
    NO_PERMISSION("10002", "common.permission.denied"),
    PARAM_ERROR("10003", "common.param.error"),
    DATA_NOT_FOUND("10004", "common.data.not.found"),
    AUTH_USER_NOT_FOUND("10005", "oauth.username.not.exits"),
    AUTH_ACCOUNT_INCORRECT("10006", "oauth.account.incorrect"),

    PASSWORD_NOT_VALID("10007", "oauth.password.not.valid");

    private String code;
    private String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
