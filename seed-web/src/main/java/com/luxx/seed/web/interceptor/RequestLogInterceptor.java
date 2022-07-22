package com.luxx.seed.web.interceptor;

import com.luxx.seed.controller.BaseController;
import com.luxx.seed.web.filter.RequestWrapper;
import com.luxx.seed.model.OprLog;
import com.luxx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String param = JsonUtil.encode(request.getParameterMap());
        String body = "";
        if (request instanceof RequestWrapper) {
            body = ((RequestWrapper) request).getBody();
        }
        String token = request.getHeader(BaseController.AUTH_TOKEN);
        String user = Optional.ofNullable(token).map(Object::toString).orElse("");
        OprLog logEntity = OprLog.builder().method(method)
                .uri(uri)
                .param(param)
                .body(body)
                .user(user).build();
        log.info("Request log: {}", logEntity.toString());
        return true;
    }

}
