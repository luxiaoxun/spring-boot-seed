package com.luxx.engine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@MapperScan("com.luxx.engine.dao")
public class EngineApp implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EngineApp.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.run(args);
    }

}