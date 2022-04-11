package com.luxx.seed.constant;

public enum OsEnum {
    unknown(0, "unknown"),
    windows(1, "windows"),
    linux(2, "windows");

    private Integer id;
    private String name;

    OsEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
