package com.luxx.seed.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class BaseController {
    public static final String USER_KEY = "login_user";
    public static final String AUTH_TOKEN = "auth-token";

    public static String getUserInfo() {
        return Optional.ofNullable(getRequest().getAttribute(USER_KEY))
                .map(Object::toString).orElse("");
    }

    public static void setUserInfo(String userId) {
        getRequest().setAttribute(USER_KEY, userId);
    }

    public void removeUserInfo() {
        getRequest().removeAttribute(USER_KEY);
    }

    protected static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
