package com.luxx.task.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = "com.luxx.task.**")
@ConditionalOnProperty(prefix = "spring.schedule.job.admin", name = "addresses")
@Slf4j
@Profile({"prod", "dev"})
public class ScheduleJobAutoConfiguration {
    @Value("${spring.schedule.job.admin.addresses}")
    private String adminAddresses;

    @Value("${spring.schedule.job.executor.app-name}")
    private String appName;

    @Value("${spring.schedule.job.executor.ip}")
    private String ip;

    @Value("${spring.schedule.job.executor.port}")
    private int port;

    @Value("${spring.schedule.job.accessToken}")
    private String accessToken;

    @Value("${spring.schedule.job.executor.log-path}")
    private String logPath;

    @Value("${spring.schedule.job.executor.log-retention-days}")
    private int logRetentionDays;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        log.info("Register job executor");
        XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
        xxlJobExecutor.setAdminAddresses(adminAddresses);
        xxlJobExecutor.setAppname(appName);
        xxlJobExecutor.setIp(ip);
        xxlJobExecutor.setPort(port);
        xxlJobExecutor.setAccessToken(accessToken);
        xxlJobExecutor.setLogPath(logPath);
        xxlJobExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobExecutor;
    }
}
