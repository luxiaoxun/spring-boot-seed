package com.luxx.seed.controller;

import com.luxx.seed.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report")
@Api(tags = "report")
@Slf4j
public class ReportController extends BaseController {
    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "获取报告")
    @GetMapping("/agent-report")
    public void getReport(@RequestParam(required = false, defaultValue = "pdf") String type, HttpServletResponse response) {
        reportService.exportReport(type, response);
    }

}
