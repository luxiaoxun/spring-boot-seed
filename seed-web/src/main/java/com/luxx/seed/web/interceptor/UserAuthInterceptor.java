package com.luxx.seed.web.interceptor;

import com.luxx.seed.config.annotation.NoNeedAuth;
import com.luxx.seed.controller.BaseController;
import com.luxx.seed.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        //加了NoNeedAuth注解的方法,不需要授权
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().getAnnotation(NoNeedAuth.class) != null) {
                return true;
            }
        }

        String token = request.getHeader(BaseController.AUTH_TOKEN);
        // 执行认证
        if (token == null) {
            log.info("No token, please login, request uri: " + request.getRequestURI());
            response.sendError(401, "No auth token");
            return false;
        }
        // 验证 token, 获取 token 中的 user id
        String userId = TokenUtil.verifyToken(token);
        if (StringUtils.isEmpty(userId)) {
            log.warn("Token verify failed");
            response.sendError(401, "Token verify failed");
            return false;
        } else {
            request.setAttribute(BaseController.USER_KEY, userId);
        }
        return true;
    }
}
