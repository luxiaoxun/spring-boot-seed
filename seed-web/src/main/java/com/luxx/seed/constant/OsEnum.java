package com.luxx.seed.constant;

public enum OsEnum {
    unknown(0, "unknown"),
    linux(1, "linux"),
    windows(2, "windows");

    private Integer id;
    private String name;

    OsEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
