package com.luxx.seed.web.filter;

import cn.hutool.core.lang.ObjectId;
import com.luxx.seed.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@Component
//@WebFilter(urlPatterns = "/*")
//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class RequestIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String reqId = httpServletRequest.getHeader(WebUtil.REQ_ID_HEADER);
            //没有则生成一个
            if (StringUtils.isEmpty(reqId)) {
                reqId = ObjectId.next();
            }
            log.info("Request id:" + reqId);
            WebUtil.setRequestId(reqId);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            WebUtil.removeRequestId();
        }
    }
}
