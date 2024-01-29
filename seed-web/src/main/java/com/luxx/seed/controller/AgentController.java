package com.luxx.seed.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.config.i18n.I18nMessageUtil;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.Agent;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.AgentService;
import com.luxx.seed.util.WebUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
@Tag(name = "agent")
@Slf4j
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Operation(summary = "统计Agent数量")
    @GetMapping("/count")
    public Response getAgentCount() {
        log.info("{} get agent count", StpUtil.getTokenInfo().loginId);
        long count = agentService.getAgentCount();
        return ResponseUtil.success(count);
    }

    @Operation(summary = "分页查询Agent")
    @GetMapping("/search/page")
    public Response getAgentByPage(@RequestParam(required = false) String type,
                                   @RequestParam(defaultValue = "id") String order,
                                   @RequestParam(defaultValue = "ASC") String direction,
                                   @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        log.info("{} get agent by page", StpUtil.getTokenInfo().loginId);
        if (!Constant.SORT_ASC.equals(direction) && !Constant.SORT_DESC.equals(direction)) {
            return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Agent> agentList = agentService.getAgentsByType(type, order, direction);
        PageInfo<Agent> pageInfo = new PageInfo<>(agentList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("data", pageInfo.getList());
        return ResponseUtil.success(map);
    }

    @Operation(summary = "根据IP查询Agent")
    @GetMapping("/search")
    public Response getAgentByIp(@RequestParam String ip) {
        Agent agent = agentService.findByIp(ip);
        return ResponseUtil.success(agent);
    }

    @Operation(summary = "创建Agent")
    @PostMapping("/create")
    public Response createAgent(@RequestBody Agent agent) {
        try {
            agentService.createAgent(agent);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create agent error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "test")
    @GetMapping("/test")
    public Response test() {
        log.info("Request Id: " + WebUtil.getRequestId());
        log.info("Default local message: " + I18nMessageUtil.getMsg("common.success"));
        return ResponseUtil.success();
    }

}
