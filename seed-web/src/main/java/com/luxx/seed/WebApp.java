package com.luxx.seed;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.luxx.seed.dao")
public class WebApp implements WebMvcConfigurer {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(WebApp.class).run(args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePathPatterns = {"/auth/login", "/auth/login/captcha", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**"};
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch("*.html")
                    .notMatch("*.js")
                    .notMatch("*.css")
                    .check(r -> StpUtil.checkLogin());
        })).excludePathPatterns(excludePathPatterns);

        // 国际化语言拦截器
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("language");
        registry.addInterceptor(localeInterceptor);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(150));
        factory.setMaxRequestSize(DataSize.ofMegabytes(150));
        return factory.createMultipartConfig();
    }
}