package com.luxx.workflow.core.nodeDefinition;

import com.luxx.workflow.core.enums.NodeEnum;
import lombok.NonNull;

/**
 * @Author: jiangtao
 * @Date 2024/4/7 11:33
 * @Description:
 */
public class WhenNode extends Node {

    public WhenNode(@NonNull String id, @NonNull String name) {
        super(id, name, NodeEnum.WHEN);
    }
}
