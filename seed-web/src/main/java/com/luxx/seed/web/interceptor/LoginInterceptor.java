package com.luxx.seed.web.interceptor;

import com.luxx.seed.controller.BaseController;
import com.luxx.seed.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (profile.equals("dev")) {
            return true;
        }
        String user = request.getHeader("user");
        if (user != null) {
            request.getSession().setAttribute(BaseController.USERNAME_KEY_IN_SESSION, user);
            return true;
        }
        Object userName = request.getSession().getAttribute(BaseController.USERNAME_KEY_IN_SESSION);
        if (userName == null) {
            log.error("用户请求{}被拦截", request.getRequestURI());
            response.sendError(401);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
