package com.luxx.seed.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Profile("dev")
@RequestMapping("task")
@Api(tags = "task")
public class TaskCallController {
//    @Resource
//    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @ApiOperation(value = "test")
    @PostMapping("/test")
    public void test(String beanName) throws Exception {
//        beanName = beanName.replaceFirst(beanName.substring(0, 1), beanName.substring(0, 1).toLowerCase());
//        IJobHandler jh = (IJobHandler) autowireCapableBeanFactory.getBean(beanName);
//        jh.execute(null);
    }
}
