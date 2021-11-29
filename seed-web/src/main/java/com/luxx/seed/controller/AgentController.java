package com.luxx.seed.controller;

import com.luxx.seed.model.AgentEntity;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.AgentService;
import com.luxx.seed.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
@Api(tags = "agent")
@Slf4j
public class AgentController extends BaseController {

    @Autowired
    private AgentService agentService;

    @ApiOperation(value = "统计Agent数量", notes = "统计Agent数量")
    @GetMapping("/count")
    public Response getAgentCount() {
        long count = agentService.getAgentCount();
        return ResponseUtil.success(count);
    }

    @ApiOperation(value = "根据IP查询Agent", notes = "根据IP查询Agent")
    @GetMapping("/search")
    public Response getAgentByIp(@RequestParam String ip) {
        AgentEntity agent = agentService.findByIp(ip);
        return ResponseUtil.success(agent);
    }

    @ApiOperation(value = "创建Agent", notes = "创建Agent")
    @PostMapping("/create")
    public Response createAgent(@RequestBody AgentEntity agent) {
        try {
            agentService.createAgent(agent);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create agent error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @ApiOperation(value = "test", notes = "test")
    @GetMapping("/test")
    public String test(@RequestParam String msg) {
        log.info("Request Id: " + WebUtil.getRequestId());
        log.info("Request user id: " + getUserInfo());
        log.info(msg);
        return msg;
    }

}
