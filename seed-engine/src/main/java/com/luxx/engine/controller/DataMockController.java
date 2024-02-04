package com.luxx.engine.controller;

import com.luxx.engine.response.Response;
import com.luxx.engine.response.ResponseUtil;
import com.luxx.engine.service.DataMockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "mock")
@RequestMapping("/mock")
@Slf4j
@Profile("dev")
public class DataMockController {
    @Autowired
    private DataMockService dataMockService;

    @Operation(summary = "发送mock数据")
    @PostMapping("/data")
    public Response sendEventMockData(@RequestParam(defaultValue = "1") Integer threads,
                                      @RequestParam(defaultValue = "10") Integer count) {
        log.info(String.format("%s threads try to send %s mock data", threads, count));
        dataMockService.sendEventMockData(threads, count);
        return ResponseUtil.success();
    }

    @Operation(summary = "发送json mock数据")
    @PostMapping("/data/json")
    public Response sendMockData(@RequestBody String jsonData) {
        dataMockService.sendEventMockData(jsonData);
        return ResponseUtil.success();
    }

}
