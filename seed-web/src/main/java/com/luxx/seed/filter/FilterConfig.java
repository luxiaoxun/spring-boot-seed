package com.luxx.seed.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean registerAuditLogFilter() {
        FilterRegistrationBean<AuditLogFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AuditLogFilter());
        bean.setName("auditLogFilter");
        bean.setOrder(2);
        //匹配url
        bean.addUrlPatterns("/*");
        return bean;
    }
}
