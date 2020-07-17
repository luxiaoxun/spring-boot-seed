package com.luxx.seed.web.filter;

import io.swagger.models.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
public class ChannelFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            if (!((HttpServletRequest) servletRequest).getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
                ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
                filterChain.doFilter(requestWrapper, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
