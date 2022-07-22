package com.luxx.seed.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.Agent;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.AgentService;
import com.luxx.seed.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
@Api(tags = "agent")
@Slf4j
public class AgentController extends BaseController {

    @Autowired
    private AgentService agentService;

    @ApiOperation(value = "统计Agent数量")
    @GetMapping("/count")
    public Response getAgentCount() {
        long count = agentService.getAgentCount();
        return ResponseUtil.success(count);
    }

    @ApiOperation(value = "分页查询Agent")
    @GetMapping("/search/page")
    public Response getAgentByPage(@RequestParam(required = false) String type,
                                   @RequestParam(defaultValue = "id") String order,
                                   @RequestParam(defaultValue = "ASC") String direction,
                                   @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        if (!Constant.SORT_ASC.equals(direction) && !Constant.SORT_DESC.equals(direction)) {
            return ResponseUtil.fail(ResponseCode.PARAM_ILLEGAL);
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

    @ApiOperation(value = "根据IP查询Agent")
    @GetMapping("/search")
    public Response getAgentByIp(@RequestParam String ip) {
        Agent agent = agentService.findByIp(ip);
        return ResponseUtil.success(agent);
    }

    @ApiOperation(value = "创建Agent")
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

    @ApiOperation(value = "test")
    @GetMapping("/test")
    public String test(@RequestParam String msg) {
        log.info("Request Id: " + WebUtil.getRequestId());
        log.info("Request user id: " + getUserInfo());
        log.info(msg);
        return msg;
    }

}
