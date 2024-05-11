package com.luxx.seed.response;

public enum ResponseCode {
    SUCCESS("200", "common_success"),
    SYSTEM_ERROR("10001", "common_fail"),
    NO_PERMISSION("10002", "common_permission_denied"),
    PARAM_ERROR("10003", "common_param_error"),
    DATA_NOT_FOUND("10004", "common_data_not_found"),

    AUTH_USER_NOT_FOUND("10101", "oauth_account_not_exits"),
    AUTH_ACCOUNT_INCORRECT("10102", "oauth_account_incorrect"),
    AUTH_ACCOUNT_LOCKED("10103", "oauth_account_locked"),
    AUTH_ACCOUNT_ILLEGAL("10104", "oauth_account_illegal"),

    ACCOUNT_NOT_VALID("10201", "account_not_valid"),
    PASSWORD_NOT_VALID("10202", "password_not_valid"),
    ROLE_NOT_VALID("10203", "role_not_valid");

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
