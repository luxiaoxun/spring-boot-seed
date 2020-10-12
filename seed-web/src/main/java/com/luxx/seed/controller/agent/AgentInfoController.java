package com.luxx.seed.controller.agent;

import com.luxx.seed.controller.BaseController;
import com.luxx.seed.jpa.entity.AgentEntity;
import com.luxx.seed.jpa.service.AgentEntityService;
import com.luxx.seed.service.agent.AgentService;
import com.luxx.seed.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
@Api(tags = "agent")
@Slf4j
public class AgentInfoController extends BaseController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentEntityService agentEntityService;

    @ApiOperation(value = "统计Agent数量", notes = "统计Agent数量")
    @GetMapping("/count")
    public long getAgent() {
        return 100;
//        return agentService.getAgentNumber();
    }

    @ApiOperation(value = "根据IP查询Agent", notes = "根据IP查询Agent")
    @GetMapping("/search")
    public AgentEntity getAgentByIp(@RequestParam String ip) {
        return agentEntityService.findByIp(ip);
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
