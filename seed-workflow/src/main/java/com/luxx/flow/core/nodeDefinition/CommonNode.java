package com.luxx.flow.core.nodeDefinition;

import com.luxx.flow.core.enums.NodeEnum;
import lombok.NonNull;

public class CommonNode extends Node {

    public CommonNode(@NonNull String id, @NonNull String name) {
        super(id, name, NodeEnum.COMMON);
    }
}
