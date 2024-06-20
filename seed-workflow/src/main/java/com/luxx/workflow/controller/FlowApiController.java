package com.luxx.workflow.controller;

import com.luxx.workflow.core.ElHelper;
import com.luxx.workflow.core.model.JsonAstModel;
import com.luxx.workflow.core.nodeDefinition.Node;
import com.luxx.workflow.response.Response;
import com.luxx.workflow.response.ResponseUtil;
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
@RequestMapping("flow")
@Slf4j
public class FlowApiController {

    @PostMapping("/toEl")
    public Response toEl(@RequestBody JsonAstModel request) {
        try {
            // 1、用前端的json构建基于Node的抽象语法树
            Node head = ElHelper.Json2Node(request);
            // 2、将ast转为EL表达式
            String el = ElHelper.ast2El(head);
            log.info("el:{}", el);
            // 3、校验EL表达式的有效性
            if (LiteFlowChainELBuilder.validate(el)) {
                return ResponseUtil.success(el);
            } else {
                return ResponseUtil.fail();
            }
        } catch (Exception e) {
            log.error("生成El表达式异常", e);
            return ResponseUtil.fail();
        }
    }
}
