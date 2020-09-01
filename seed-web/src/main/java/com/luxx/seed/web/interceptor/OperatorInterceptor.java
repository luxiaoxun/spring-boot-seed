package com.luxx.seed.web.interceptor;

import com.luxx.seed.controller.BaseController;
import com.luxx.seed.web.filter.RequestWrapper;
import com.luxx.seed.model.OprLogEntity;
import com.luxx.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class OperatorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request instanceof RequestWrapper) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String param = JsonUtil.encode(request.getParameterMap());
            String body = ((RequestWrapper) request).getBody();
            String user = Optional.ofNullable(request.getSession().getAttribute(BaseController.USERNAME_KEY_IN_SESSION))
                    .map(Object::toString)
                    .orElse("unknown");
            OprLogEntity logEntity = OprLogEntity.builder().method(method)
                    .uri(uri)
                    .param(param)
                    .body(body)
                    .user(user).build();
            log.info("用户请求信息为{}", logEntity);
        }
        return true;
    }

}
