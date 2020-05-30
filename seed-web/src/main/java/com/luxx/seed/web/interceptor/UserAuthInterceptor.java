package com.luxx.seed.web.interceptor;

import com.luxx.seed.config.annotation.NoNeedAuthority;
import com.luxx.seed.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

//    @Autowired
//    UserService userService;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (profile.equals("dev")) {
            return true;
        }
        //加了NoNeedAuthority注解,不需要校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().getAnnotation(NoNeedAuthority.class) != null) {
                return true;
            }
        }

        Object userNo = request.getSession().getAttribute(BaseController.USERNAME_KEY_IN_SESSION);
        if (userNo == null) {
            log.info("【权限拦截器执行,用户为空】");
            response.sendError(401, "没有登录");
            return false;
        }

        return true;
    }
}
