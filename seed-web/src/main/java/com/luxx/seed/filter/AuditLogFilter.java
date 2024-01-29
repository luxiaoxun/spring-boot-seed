package com.luxx.seed.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.filter.wrapper.RequestWrapper;
import com.luxx.seed.filter.wrapper.ResponseWrapper;
import com.luxx.seed.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AuditLogFilter implements Filter {
    private static final List<String> IP_HEADERS = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

    public static String getClientIp(HttpServletRequest request) {
        return IP_HEADERS.stream().map(request::getHeader).filter(xff -> !ObjectUtils.isEmpty(xff)).map(ipString -> ipString.split(",")[0].trim()).filter(ip -> !ip.equalsIgnoreCase("unknown")).findFirst().orElseGet(request::getRemoteAddr);
    }

    public AuditLogFilter() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
            request = new StandardServletMultipartResolver().resolveMultipart(request);
        }
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);

        String requestUrl = requestWrapper.getRequestURI();
        if (StringUtils.hasLength(requestUrl) && uriValidCheck(requestUrl)) {
            AuditLog auditLog = new AuditLog();
            auditLog.setCustomerInfo("");
            auditLog.setUserId(StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "");
            auditLog.setRequestUri(requestUrl);
            auditLog.setRequestName("");
            auditLog.setRequestIp(getClientIp(request));
            auditLog.setRequestTime(DateTimeUtil.getCurrentTimeForFormat(DateTimeUtil.YMD_HMS));
            auditLog.setExecTime(System.currentTimeMillis() - startTime);
            auditLog.setResponseHttpCode(responseWrapper.getStatus());
            auditLog.setVersion(Constant.PRODUCT_VERSION);
            log.info("AuditLog: {}", auditLog);
        }
    }

    private boolean uriValidCheck(String uri) {
        return !uri.contains("/favicon.ico") && !uri.contains("/swagger-ui") &&
                !uri.contains("/v3/api-docs");
    }

    @Override
    public void destroy() {

    }
}