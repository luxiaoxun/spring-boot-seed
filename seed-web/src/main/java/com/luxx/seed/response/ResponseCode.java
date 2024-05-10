package com.luxx.seed.response;

public enum ResponseCode {
    SUCCESS("200", "common_success"),
    SYSTEM_ERROR("10001", "common_fail"),
    NO_PERMISSION("10002", "common_permission_denied"),
    PARAM_ERROR("10003", "common_param_error"),
    DATA_NOT_FOUND("10004", "common_data_not_found"),
    AUTH_USER_NOT_FOUND("10005", "oauth_username_not_exits"),
    AUTH_ACCOUNT_INCORRECT("10006", "oauth_account_incorrect"),

    PASSWORD_NOT_VALID("10007", "oauth_password_not_valid");

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
