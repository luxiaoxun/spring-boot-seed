package com.luxx.seed.response;

public enum ResponseCode {
    SUCCESS("200", "success"),
    FAIL("10001", "fail"),
    NO_PERMISSION("10002", "common.permission.denied"),
    PARAM_ERROR("10003", "common.param.error"),
    DATA_NOT_FOUND("10004", "common.data.not.found"),

    AUTH_USER_NOT_FOUND("10101", "auth.account.not.exits"),
    AUTH_CAPTCHA_ERROR("10102", "auth.captcha.incorrect"),
    AUTH_ACCOUNT_INCORRECT("10103", "auth.account.incorrect"),
    AUTH_ACCOUNT_LOCKED("10104", "auth.account.locked"),
    AUTH_ACCOUNT_ILLEGAL("10105", "auth.account.illegal"),

    ACCOUNT_NOT_VALID("10201", "account.not.valid"),
    PASSWORD_NOT_VALID("10202", "password.not.valid"),
    ROLE_NOT_VALID("10203", "role.not.valid");

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
