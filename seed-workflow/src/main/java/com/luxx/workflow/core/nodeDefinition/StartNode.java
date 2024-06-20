package com.luxx.workflow.core.nodeDefinition;

import com.luxx.workflow.core.enums.NodeEnum;
import lombok.NonNull;

/**
 * @Author: jiangtao
 * @Date 2024/5/7 20:50
 * @Description:
 */
public class StartNode extends Node {

    public StartNode(@NonNull String id, @NonNull String name) {
        super(id, name, NodeEnum.START);
    }
}
