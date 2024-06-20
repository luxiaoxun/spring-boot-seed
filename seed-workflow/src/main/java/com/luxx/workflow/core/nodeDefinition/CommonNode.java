package com.luxx.workflow.core.nodeDefinition;

import com.luxx.workflow.core.enums.NodeEnum;
import lombok.NonNull;

public class CommonNode extends Node {

    public CommonNode(@NonNull String id, @NonNull String name) {
        super(id, name, NodeEnum.COMMON);
    }
}
