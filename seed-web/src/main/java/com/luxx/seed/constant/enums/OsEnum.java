package com.luxx.seed.constant.enums;

public enum OsEnum {
    unknown(0, "unknown"),
    linux(1, "linux"),
    windows(2, "windows");

    private Integer code;
    private String name;

    OsEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
