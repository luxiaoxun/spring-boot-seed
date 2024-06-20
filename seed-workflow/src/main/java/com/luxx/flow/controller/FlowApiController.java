package com.luxx.flow.controller;

import com.luxx.flow.core.ElHelper;
import com.luxx.flow.core.model.JsonAstModel;
import com.luxx.flow.core.nodeDefinition.Node;
import com.luxx.flow.response.Response;
import com.luxx.flow.response.ResponseUtil;
import com.yomahub.liteflow.builder.el.LiteFlowChainELBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jiangtao
 * @Date 2024/4/2 21:18
 * @Description:
 */
@RestController
@RequestMapping("liteflow")
@Slf4j
public class FlowApiController {

    @PostMapping("/toEl")
    public Response toEl(@RequestBody JsonAstModel request) {
        try {
            // 1、用前端的json构建基于Node的抽象语法树
            Node head = ElHelper.Json2Node(request);
            // 2、将ast转为EL表达式
            String el = ElHelper.ast2El(head);
            System.out.println(el);
            // 3、校验EL表达式的有效性
            return ResponseUtil.success(LiteFlowChainELBuilder.validate(el));
        } catch (Exception e) {
            log.error("[生成El表达式] 发生异常", e);
            return ResponseUtil.fail();
        }
    }
}
