package com.luxx.flow.core.function;

import com.luxx.flow.core.enums.NodeEnum;
import com.luxx.flow.core.nodeDefinition.Node;

import java.util.function.Supplier;

/**
 * @Author: jiangtao
 * @Date 2024/4/3 15:44
 * @Description:统一节点处理方式，以后扩展节点不用全量找了
 */
public class NodeFunction {

    /**
     * 有返回值
     *
     * @param nodeEnum
     * @param commonNodeSupplier
     * @param ifNodeSupplier
     * @return
     */
    public static Node createNode(NodeEnum nodeEnum, Supplier<Node> commonNodeSupplier, Supplier<Node> whenNodeSupplier, Supplier<Node> ifNodeSupplier, Supplier<Node> switchNodeSupplier, Supplier<Node> summarySupplier, Supplier<Node> startSupplier, Supplier<Node> endSupplier) {
        switch (nodeEnum) {
            case COMMON:
                return commonNodeSupplier.get();
            case WHEN:
                return whenNodeSupplier.get();
            case IF:
                return ifNodeSupplier.get();
            case SWITCH:
                return switchNodeSupplier.get();
            case SUMMARY:
                return summarySupplier.get();
            case START:
                return startSupplier.get();
            case END:
                return endSupplier.get();
            default:
                throw new RuntimeException("未知节点类型：" + nodeEnum);
        }
    }

    /**
     * 无返回值
     *
     * @param nodeEnum
     * @param commonNodeRunnable
     * @param ifNodeRunnable
     */
    public static void dealNode(NodeEnum nodeEnum, Runnable commonNodeRunnable, Runnable whenNodeRunnable, Runnable ifNodeRunnable, Runnable switchRunnable, Runnable summaryRunnable, Runnable startRunnable, Runnable endRunnable) {
        switch (nodeEnum) {
            case COMMON:
                commonNodeRunnable.run();
                break;
            case WHEN:
                whenNodeRunnable.run();
                break;
            case IF:
                ifNodeRunnable.run();
                break;
            case SWITCH:
                switchRunnable.run();
                break;
            case SUMMARY:
                summaryRunnable.run();
                break;
            case START:
                startRunnable.run();
                break;
            case END:
                endRunnable.run();
                break;
            default:
                throw new RuntimeException("未知节点类型：" + nodeEnum);
        }
    }
}
