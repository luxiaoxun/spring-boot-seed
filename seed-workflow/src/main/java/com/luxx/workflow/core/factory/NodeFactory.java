package com.luxx.workflow.core.factory;

import com.luxx.workflow.core.enums.NodeEnum;
import com.luxx.workflow.core.function.NodeFunction;
import com.luxx.workflow.core.nodeDefinition.*;

/**
 * @Author: jiangtao
 * @Date 2024/4/3 10:57
 * @Description:
 */
public class NodeFactory {

    public static Node getNode(NodeEnum nodeEnum, String id, String name) {
        return NodeFunction.createNode(nodeEnum,
                () -> new CommonNode(id, name),
                () -> new WhenNode(id, name),
                () -> new IfNode(id, name),
                () -> new SwitchNode(id, name),
                () -> new SummaryNode(id, name),
                () -> new StartNode(id, name),
                () -> new EndNode(id, name));
    }
}
