package com.luxx.seed.web.filter;

import cn.hutool.core.lang.ObjectId;
import com.luxx.util.WebUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*", filterName = "requestIdFilter")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class RequestIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String reqId = httpServletRequest.getHeader(WebUtil.REQ_ID_HEADER);
        //没有则生成一个
        if (StringUtils.isEmpty(reqId)) {
            reqId = ObjectId.next();
        }
        WebUtil.setRequestId(reqId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            WebUtil.removeRequestId();
        }
    }
}
