package com.luxx.seed.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TaskStatus {
    INIT(-1, "创建中"),
    CREATE(0, "已创建"),
    SUCCESS(1, "完成"),
    PROCESS(2, "处理中"),
    FAIL(3, "失败");

    private Integer id;
    private String name;

    TaskStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private static Map<Integer, TaskStatus> allTaskStatus = new HashMap<>();

    static {
        for (TaskStatus t : TaskStatus.values()) {
            allTaskStatus.put(t.id, t);
        }
    }

    public static TaskStatus getTaskStatusTypeById(Integer id) {
        return allTaskStatus.get(id);
    }

}
