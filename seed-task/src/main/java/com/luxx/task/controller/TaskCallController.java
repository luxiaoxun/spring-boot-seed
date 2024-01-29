package com.luxx.task.controller;

import com.xxl.job.core.handler.IJobHandler;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
@RequestMapping("/task")
public class TaskCallController {
    @Resource
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @PostMapping("/test")
    public void test(String beanName) throws Exception {
        beanName = beanName.replaceFirst(beanName.substring(0, 1), beanName.substring(0, 1).toLowerCase());
        IJobHandler jh = (IJobHandler) autowireCapableBeanFactory.getBean(beanName);
        jh.execute();
    }
}
