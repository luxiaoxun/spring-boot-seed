package com.luxx.flow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@EnableScheduling
@MapperScan("com.luxx.flow.dao")
public class FlowApp implements WebMvcConfigurer {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication springApplication = new SpringApplication(FlowApp.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.run(args);
    }
}