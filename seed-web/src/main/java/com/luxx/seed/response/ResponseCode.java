package com.luxx.seed.response;

public enum ResponseCode {
    SUCCESS("0", "success"),
    UNKNOWN_ERROR("10000", "未知错误"),
    SYSTEM_ERROR("10001", "系统处理异常"),
    REQUEST_ERROR("10002", "请求地址或参数错误"),
    DB_PROCESS_FAILED("10003", "数据库处理失败"),
    DATA_NOT_FOUND("10004", "数据不存在"),
    PARAM_REQUIRED("10005", "参数必填[%s]"),
    PARAM_ILLEGAL("10006", "参数格式错误[%s]"),
    FILE_PROCESS_ERROR("10007", "文件处理失败"),
    FILE_SAVE_FAILED("10008", "文件保存失败"),
    FILE_GET_FAILED("10009", "文件获取失败"),
    LOGIN_TIMEOUT("10010", "登录超时，请重新登录！"),
    NO_PERMISSION("10011", "无访问权限"),

    USER_NAME_EXISTS("10100", "用户名已存在"),
    USER_NAME_NOT_EXISTS("10101", "用户名不存在"),
    PASSWORD_INCORRECT("10102", "密码错误"),
    LOGIN_FAILED("10103", "登录失败，用户名或密码错误"),
    OLD_PASSWORD_ERROR("10104", "旧密码错误"),

    CUSTOMER_NUMBER_EXISTS("20100", "客户编号已存在"),
    CARD_NUMBER_EXISTS("20101", "证件号码已存在");

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
