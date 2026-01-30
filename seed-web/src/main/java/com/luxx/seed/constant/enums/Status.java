package com.luxx.seed.constant.enums;

import com.luxx.seed.config.i18n.I18nEnum;
import lombok.Getter;

@Getter
public enum Status implements I18nEnum {
    DISABLED(0, "无效"),
    ENABLED(1, "有效"),
    DELETED(2, "删除"),
    LOCKED(3, "锁定");

    private Integer code;
    private String name;

    Status(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
