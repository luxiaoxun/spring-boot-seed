package com.luxx.seed.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class BaseController {

    public static final String USERNAME_KEY_IN_SESSION = "_login_user_";

    protected String getCurrentUserName() {
        return Optional.ofNullable(getSession().getAttribute(USERNAME_KEY_IN_SESSION))
                .map(Object::toString)
                .orElse("luxiaoxun");
    }

    protected void setUserInfoInSession(String userName) {
        HttpSession session = getSession();
        // session time out 30 minutes
        session.setMaxInactiveInterval(30 * 60);
        session.setAttribute(USERNAME_KEY_IN_SESSION, userName);
    }

    protected void removeUserInfoInSession() {
        getSession().removeAttribute(USERNAME_KEY_IN_SESSION);
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
