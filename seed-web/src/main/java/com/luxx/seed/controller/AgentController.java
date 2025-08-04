package com.luxx.seed.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.config.i18n.I18nMessageUtil;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.Agent;
import com.luxx.seed.request.AgentRequest;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.AgentService;
import com.luxx.seed.util.WebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Min;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
@Slf4j
public class AgentController {

    @Autowired
    private AgentService agentService;

    @SaCheckPermission("agent")
    @GetMapping("/count")
    public Response getAgentCount() {
        log.info("{} get agent count", StpUtil.getTokenInfo().loginId);
        long count = agentService.getAgentCount();
        return ResponseUtil.success(count);
    }

    @GetMapping("/page-list")
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

    @GetMapping("/search")
    public Response getAgentByIp(@RequestParam String ip) {
        Agent agent = agentService.findByIp(ip);
        return ResponseUtil.success(agent);
    }

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

    @PostMapping("/export")
    public void exportAgents(@RequestBody AgentRequest request, HttpServletResponse response) {
        try {
            List<Agent> agentList = agentService.getAgentsByType(request.getType(), "id", Constant.SORT_ASC);
            response.setContentType("application/csv");
            response.setCharacterEncoding("UTF-8");
            String fileName = "agent-list-" + Instant.now().toEpochMilli() + ".csv";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream(), Agent.class).excelType(ExcelTypeEnum.CSV)
                    .sheet("Sheet1").doWrite(agentList);
        } catch (Exception ex) {
            log.error("Export agents error: " + ex.toString());
        }
    }

    @GetMapping("/test")
    public Response test() {
        log.info("Request Id: " + WebUtil.getRequestId());
        log.info("Default local message: " + I18nMessageUtil.getMsg(ResponseCode.SUCCESS.getMsg()));
        return ResponseUtil.success();
    }

}
