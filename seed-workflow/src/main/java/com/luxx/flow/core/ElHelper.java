package com.luxx.flow.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.luxx.flow.core.enums.NodeEnum;
import com.luxx.flow.core.factory.NodeFactory;
import com.luxx.flow.core.function.NodeFunction;
import com.luxx.flow.core.model.JsonAstModel;
import com.luxx.flow.core.nodeDefinition.IfNode;
import com.luxx.flow.core.nodeDefinition.Node;
import com.luxx.flow.core.nodeDefinition.SwitchNode;
import com.yomahub.liteflow.builder.el.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ElHelper {

    /**
     * nodeAst转El表达式
     *
     * @param head
     * @return
     */
    public static String ast2El(Node head) {
        if (head == null) {
            return null;
        }
        // 用于存放if、when、switch节点的ThenELWrapper
        Deque<ThenELWrapper> stack = new ArrayDeque<>();
        // 用于标记是否处理过summary节点了
        Set<String> doneSummary = Sets.newHashSet();
        return tree2El(head, ELBus.then(), stack, doneSummary).toEL();
    }

    private static ThenELWrapper tree2El(Node currentNode, ThenELWrapper currentThenElWrapper, Deque<ThenELWrapper> stack, Set<String> doneSummary) {
        NodeFunction.dealNode(currentNode.getNodeEnum(),
                // common
                () -> {
                    currentThenElWrapper.then(currentNode.getName());
                    for (Node nextNode : currentNode.getNext()) {
                        tree2El(nextNode, currentThenElWrapper, stack, doneSummary);
                    }
                },
                // when
                () -> {
                    stack.push(currentThenElWrapper);
                    WhenELWrapper whenELWrapper = ELBus.when().ignoreError(Boolean.TRUE);
                    currentThenElWrapper.then(whenELWrapper);
                    for (Node nextNode : currentNode.getNext()) {
                        ThenELWrapper thenELWrapper = ELBus.then();
                        whenELWrapper.when(thenELWrapper);
                        tree2El(nextNode, thenELWrapper, stack, doneSummary);
                    }
                },
                // if
                () -> {
                    stack.push(currentThenElWrapper);
                    ThenELWrapper trueThenElWrapper = ELBus.then();
                    ThenELWrapper falseThenElWrapper = ELBus.then();
                    // 兼容可能false分支没有节点，这里只兼容false分支，官方认为true分支强制是有节点的
                    ELWrapper ignore = NodeEnum.SUMMARY.equals(convert2IfNode(currentNode).getFalseNode().getNodeEnum())
                            ? currentThenElWrapper.then(ELBus.ifOpt(currentNode.getName(), trueThenElWrapper))
                            : currentThenElWrapper.then(ELBus.ifOpt(currentNode.getName(), trueThenElWrapper, falseThenElWrapper));
                    tree2El(convert2IfNode(currentNode).getTrueNode(), trueThenElWrapper, stack, doneSummary);
                    tree2El(convert2IfNode(currentNode).getFalseNode(), falseThenElWrapper, stack, doneSummary);
                },
                // switch
                () -> {
                    stack.push(currentThenElWrapper);
                    SwitchNode switchNode = convert2SwitchNode(currentNode);
                    SwitchELWrapper switchELWrapper = ELBus.switchOpt(currentNode.getName());
                    currentThenElWrapper.then(switchELWrapper);
                    for (Map.Entry<Node, String> nextNodeTag : switchNode.getNodeTagMap().entrySet()) {
                        ThenELWrapper wrapper = ELBus.then().id(nextNodeTag.getValue());
                        switchELWrapper.to(wrapper);
                        tree2El(nextNodeTag.getKey(), wrapper, stack, doneSummary);
                    }
                },
                // summary
                () -> {
                    if (!doneSummary.contains(currentNode.getName())) {
                        doneSummary.add(currentNode.getName());
                        // 这种节点只有0个或者1个nextNode
                        for (Node nextNode : currentNode.getNext()) {
                            tree2El(nextNode, stack.pop(), stack, doneSummary);
                        }
                    }
                },
                // start
                () -> {
                    for (Node nextNode : currentNode.getNext()) {
                        tree2El(nextNode, currentThenElWrapper, stack, doneSummary);
                    }
                },
                // end
                () -> {
                });
        return currentThenElWrapper;
    }

    /**
     * 将JsonAst转为NodeAst
     *
     * @param model
     * @return
     */
    public static Node Json2Node(JsonAstModel model) {
        // 参数校验
        checkJsonAst(model);
        List<JsonAstModel.NodeEntity> nodeEntities = model.getNodeEntities();
        List<JsonAstModel.NodeEdge> nodeEdges = Optional.ofNullable(model.getNodeEdges()).orElse(Lists.newArrayList());
        // 创建node
        Map<String, Node> nodeMap = nodeEntities.stream()
                .map(nodeEntity -> NodeFactory.getNode(NodeEnum.valueByName(nodeEntity.getNodeType()), nodeEntity.getId(), nodeEntity.getName()))
                .collect(Collectors.toMap(Node::getId, Function.identity()));
        // 构建节点关系
        for (JsonAstModel.NodeEdge nodeEdge : nodeEdges) {
            Node sourceNode = nodeMap.get(nodeEdge.getSource());
            Node targetNode = nodeMap.get(nodeEdge.getTarget());
            // 不同节点处理方式不同
            NodeFunction.dealNode(sourceNode.getNodeEnum(),
                    () -> sourceNode.addNextNode(targetNode),
                    () -> sourceNode.addNextNode(targetNode),
                    () -> {
                        if (Boolean.TRUE.equals(nodeEdge.getIfNodeFlag())) {
                            convert2IfNode(sourceNode).setTrueNode(targetNode);
                        } else {
                            convert2IfNode(sourceNode).setFalseNode(targetNode);
                        }
                    },
                    () -> convert2SwitchNode(sourceNode).putNodeTag(targetNode, nodeEdge.getTag()),
                    () -> sourceNode.addNextNode(targetNode),
                    () -> sourceNode.addNextNode(targetNode),
                    () -> sourceNode.addNextNode(targetNode));
            // 设置前置节点
            targetNode.addPreNode(sourceNode);
        }
        // 只能有一个开始结点
        List<Node> start = nodeMap.values().stream().filter(node -> NodeEnum.START.equals(node.getNodeEnum())).collect(Collectors.toList());
        if (start.size() != 1) {
            throw new RuntimeException("语法树不合法，有且只能有一个开始结点！");
        }
        // 只能有一个结束结点
        List<Node> end = nodeMap.values().stream().filter(node -> NodeEnum.END.equals(node.getNodeEnum())).collect(Collectors.toList());
        if (end.size() != 1) {
            throw new RuntimeException("语法树不合法，有且只能有一个结束结点！");
        }
        for (Node node : nodeMap.values()) {
            // COMMON节点的出度和入度只能是1
            if (NodeEnum.COMMON.equals(node.getNodeEnum()) && (node.getPre().size() != 1 || node.getNext().size() != 1)) {
                throw new RuntimeException("语法树不合法，普通节点的出度和入度不能大于1");
            }
            // 开始节点出度为1，结束节点入度为1
            if (NodeEnum.START.equals(node.getNodeEnum()) && (node.getPre().size() != 0 || node.getNext().size() != 1)) {
                throw new RuntimeException("语法树不合法，开始节点的出度只能为1、入度只能为0");
            }
            if (NodeEnum.END.equals(node.getNodeEnum()) && (node.getPre().size() != 1 || node.getNext().size() != 0)) {
                throw new RuntimeException("语法树不合法，结束节点的出度只能为0、入度只能为1");
            }
        }
        return start.get(0);
    }

    private static void checkJsonAst(JsonAstModel model) {
        /**非空校验*/
        if (model == null || CollectionUtils.isEmpty(model.getNodeEntities())) {
            throw new RuntimeException("语法树不能为空");
        }
        List<JsonAstModel.NodeEntity> nodeEntities = model.getNodeEntities();
        List<JsonAstModel.NodeEdge> nodeEdges = Optional.ofNullable(model.getNodeEdges()).orElse(Lists.newArrayList());
        /**必填参数校验*/
        nodeEntities.forEach(nodeEntity -> {
            if (StringUtils.isAnyBlank(nodeEntity.getId(), nodeEntity.getLabel(), nodeEntity.getName(), nodeEntity.getNodeType()) || nodeEntity.getX() == null || nodeEntity.getY() == null) {
                throw new RuntimeException("必填参数校验不通过，节点基本属性缺失" + nodeEntity.getLabel());
            }
        });
        nodeEdges.forEach(nodeEdge -> {
            if (StringUtils.isAnyBlank(nodeEdge.getSource(), nodeEdge.getTarget())) {
                throw new RuntimeException("必填参数校验不通过，边的基本属性缺失：" + nodeEdge.getSource() + "->" + nodeEdge.getTarget());
            }
        });
        /**校验节点和边的source target是否能对应上*/
        Set<String> nodeIdSet = nodeEntities.stream().map(JsonAstModel.NodeEntity::getId).collect(Collectors.toSet());
        nodeEdges.forEach(nodeEdge -> {
            if (!nodeIdSet.contains(nodeEdge.getSource()) || !nodeIdSet.contains(nodeEdge.getTarget())) {
                throw new RuntimeException("语法树不合法，边的源或目标与节点对应不上");
            }
        });
        // 节点分支相关校验
        int summaryNumber = 0;
        int branchNumber = 0;
        NodeEnum[] nodeEnums = {NodeEnum.IF, NodeEnum.SWITCH, NodeEnum.WHEN};
        for (JsonAstModel.NodeEntity nodeEntity : nodeEntities) {
            /**校验节点类型，只能是if、common、switch、when、summary、start、end*/
            NodeEnum nodeEnum = NodeEnum.valueByName(nodeEntity.getNodeType());
            if (nodeEnum == null) {
                throw new RuntimeException("语法树不合法，存在不正确的节点类型：" + nodeEntity.getNodeType());
            }
            /**校验if节点有没有ifNodeFLag的标识*/
            if (NodeEnum.IF.equals(nodeEnum)) {
                List<JsonAstModel.NodeEdge> thisNodeEdges = nodeEdges.stream().filter(nodeEdge -> nodeEntity.getId().equals(nodeEdge.getSource())).collect(Collectors.toList());
                if (thisNodeEdges.size() != 2) {
                    throw new RuntimeException("语法树不合法，该if节点分支数量不合法：" + nodeEntity.getLabel());
                }
                Boolean firstNodeFlag = thisNodeEdges.get(0).getIfNodeFlag();
                Boolean secondNodeFlag = thisNodeEdges.get(1).getIfNodeFlag();
                // 如果都不为null，并且一个为true一个为false，则符合规则
                if (firstNodeFlag == null || secondNodeFlag == null || firstNodeFlag.equals(secondNodeFlag)) {
                    throw new RuntimeException("语法树不合法，if节点必须有一个true分支和一个false分支：" + nodeEntity.getLabel());
                }
            }
            /**校验switch的出度边是否有tag,且tag不能为空*/
            if (NodeEnum.SWITCH.equals(nodeEnum)) {
                boolean anyMatch = nodeEdges.stream()
                        .filter(nodeEdge -> nodeEntity.getId().equals(nodeEdge.getSource()))
                        .anyMatch(item -> StringUtils.isBlank(item.getTag()));
                if (anyMatch) {
                    throw new RuntimeException("语法树不合法，switch节点的所有分支都必须有tag：" + nodeEntity.getLabel());
                }
            }
            /**if、when、switch节点的数量总和与summary类型节点数量总和校验*/
            if (ObjectUtils.containsElement(nodeEnums, nodeEnum)) {
                branchNumber++;
            }
            if (NodeEnum.SUMMARY.equals(nodeEnum)) {
                summaryNumber++;
            }
        }
        if (summaryNumber != branchNumber) {
            throw new RuntimeException("语法树不合法，IF、SWITCH、WHEN节点必须有SUMMARY进行汇总");
        }
    }

    private static IfNode convert2IfNode(Node node) {
        return (IfNode) node;
    }

    private static SwitchNode convert2SwitchNode(Node node) {
        return (SwitchNode) node;
    }
}
