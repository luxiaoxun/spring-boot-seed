package com.luxx.flow.core.nodeDefinition;

import com.google.common.collect.Lists;
import com.luxx.flow.core.enums.NodeEnum;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Node {

    // node的唯一id
    private final String id;

    // node名称，对应LIteFlow的Bean名称
    private final String name;

    // 入度
    private final List<Node> pre = Lists.newArrayList();

    // 节点类型
    private final NodeEnum nodeEnum;

    // 出度
    private final List<Node> next = Lists.newArrayList();

    protected Node(String id, String name, NodeEnum nodeEnum) {
        this.id = id;
        this.name = name;
        this.nodeEnum = nodeEnum;
    }

    public void addNextNode(Node node) {
        next.add(node);
    }

    public void addPreNode(Node preNode) {
        pre.add(preNode);
    }
}