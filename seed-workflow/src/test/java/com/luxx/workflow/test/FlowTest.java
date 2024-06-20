package com.luxx.workflow.test;

import com.luxx.workflow.core.ElHelper;
import com.luxx.workflow.core.model.JsonAstModel;
import com.luxx.workflow.core.nodeDefinition.Node;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import com.yomahub.liteflow.util.JsonUtil;
import org.junit.Test;

public class FlowTest {
    @Test
    public void testFlow() {
        String flowJson = "{\n" +
                "    \"nodeEntities\": [\n" +
                "        {\n" +
                "            \"id\": \"start\",\n" +
                "            \"name\": \"start\",\n" +
                "            \"label\": \"start\",\n" +
                "            \"nodeType\": \"START\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"end\",\n" +
                "            \"name\": \"end\",\n" +
                "            \"label\": \"end\",\n" +
                "            \"nodeType\": \"END\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"c\",\n" +
                "            \"name\": \"c\",\n" +
                "            \"label\": \"c\",\n" +
                "            \"nodeType\": \"IF\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"b\",\n" +
                "            \"name\": \"b\",\n" +
                "            \"label\": \"b\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"a\",\n" +
                "            \"name\": \"a\",\n" +
                "            \"label\": \"a\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"w\",\n" +
                "            \"name\": \"w\",\n" +
                "            \"label\": \"w\",\n" +
                "            \"nodeType\": \"WHEN\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"e\",\n" +
                "            \"name\": \"e\",\n" +
                "            \"label\": \"e\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"d\",\n" +
                "            \"name\": \"d\",\n" +
                "            \"label\": \"d\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"p\",\n" +
                "            \"name\": \"p\",\n" +
                "            \"label\": \"p\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"g\",\n" +
                "            \"name\": \"g\",\n" +
                "            \"label\": \"g\",\n" +
                "            \"nodeType\": \"SWITCH\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"f\",\n" +
                "            \"name\": \"f\",\n" +
                "            \"label\": \"f\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"h\",\n" +
                "            \"name\": \"h\",\n" +
                "            \"label\": \"h\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"k\",\n" +
                "            \"name\": \"k\",\n" +
                "            \"label\": \"k\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"s\",\n" +
                "            \"name\": \"s\",\n" +
                "            \"label\": \"s\",\n" +
                "            \"nodeType\": \"SUMMARY\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"m\",\n" +
                "            \"name\": \"m\",\n" +
                "            \"label\": \"m\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"j\",\n" +
                "            \"name\": \"j\",\n" +
                "            \"label\": \"j\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"o\",\n" +
                "            \"name\": \"o\",\n" +
                "            \"label\": \"o\",\n" +
                "            \"nodeType\": \"SUMMARY\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"q\",\n" +
                "            \"name\": \"q\",\n" +
                "            \"label\": \"q\",\n" +
                "            \"nodeType\": \"SUMMARY\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"r\",\n" +
                "            \"name\": \"r\",\n" +
                "            \"label\": \"r\",\n" +
                "            \"nodeType\": \"COMMON\",\n" +
                "            \"x\": 10,\n" +
                "            \"y\": 10\n" +
                "        }\n" +
                "    ],\n" +
                "    \"nodeEdges\": [\n" +
                "        {\n" +
                "            \"source\": \"start\",\n" +
                "            \"target\": \"c\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"r\",\n" +
                "            \"target\": \"end\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"c\",\n" +
                "            \"target\": \"a\",\n" +
                "            \"ifNodeFlag\": false\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"c\",\n" +
                "            \"target\": \"b\",\n" +
                "            \"ifNodeFlag\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"b\",\n" +
                "            \"target\": \"g\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"g\",\n" +
                "            \"target\": \"f\",\n" +
                "            \"tag\": \"tag1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"g\",\n" +
                "            \"target\": \"h\",\n" +
                "            \"tag\": \"tag2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"g\",\n" +
                "            \"target\": \"k\",\n" +
                "            \"tag\": \"tag3\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"k\",\n" +
                "            \"target\": \"m\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"f\",\n" +
                "            \"target\": \"o\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"h\",\n" +
                "            \"target\": \"o\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"m\",\n" +
                "            \"target\": \"o\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"o\",\n" +
                "            \"target\": \"q\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"q\",\n" +
                "            \"target\": \"r\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"a\",\n" +
                "            \"target\": \"w\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"w\",\n" +
                "            \"target\": \"d\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"w\",\n" +
                "            \"target\": \"p\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"w\",\n" +
                "            \"target\": \"e\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"d\",\n" +
                "            \"target\": \"s\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"p\",\n" +
                "            \"target\": \"s\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"e\",\n" +
                "            \"target\": \"s\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"s\",\n" +
                "            \"target\": \"j\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"source\": \"j\",\n" +
                "            \"target\": \"q\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            JsonAstModel jsonAstModel = JsonUtil.parseObject(flowJson, JsonAstModel.class);
            // 1、用前端的json构建基于Node的抽象语法树
            Node head = ElHelper.Json2Node(jsonAstModel);
            // 2、将ast转为EL表达式
            String el = ElHelper.ast2El(head);
            System.out.println(el);
            // 3、校验EL表达式的有效性
            System.out.println(LiteFlowChainELBuilder.validate(el));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
