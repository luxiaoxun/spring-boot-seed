package com.luxx.seed.constant.enums;

import com.luxx.seed.config.i18n.I18nEnum;
import lombok.Getter;

@Getter
public enum TaskStatus implements I18nEnum {
    INIT(0, "未处理"),
    CREATE(1, "已创建"),
    SUCCESS(2, "完成"),
    PROCESS(3, "处理中"),
    FAIL(4, "失败");

    private Integer code;
    private String name;

    TaskStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TaskStatus getTaskStatusByCode(Integer code) {
        if (code == null) return INIT;
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.code.equals(code)) {
                return taskStatus;
            }
        }
        return INIT;
    }

}
