package com.luxx.seed.controller;

import com.luxx.seed.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/agent-report")
    public void getReport(@RequestParam(required = false, defaultValue = "word") String type, HttpServletResponse response) {
        reportService.exportReport(type, response);
    }

}
