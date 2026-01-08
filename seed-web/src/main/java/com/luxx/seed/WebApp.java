package com.luxx.seed;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.mybatis.spring.annotation.MapperScan;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling
@EnableWebMvc
@MapperScan("com.luxx.seed.dao")
public class WebApp implements WebMvcConfigurer {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(WebApp.class).run(args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        String[] excludePathPatterns = {"/auth/login", "/auth/login/captcha", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**"};
        // 注册 Sa-Token 拦截器，按路由校验权限
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch("*.html")
                    .notMatch("*.js")
                    .notMatch("*.css")
                    .check(r -> StpUtil.checkLogin());
        })).excludePathPatterns(excludePathPatterns);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 支持通过URL参数切换语言，如 ?lang=zh_CN
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(150));
        factory.setMaxRequestSize(DataSize.ofMegabytes(150));
        return factory.createMultipartConfig();
    }
}