package com.luxx.seed.util;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.component.SpringContextHolder;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.AuditLog;
import com.luxx.seed.component.event.AuditLogEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AuditLogUtil {

    public static final int TYPE_OK = 1;

    public static final int TYPE_FAIL = 2;

    private static final List<String> IP_HEADERS = Arrays.asList(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR");

    public static String getClientIp(HttpServletRequest request) {
        return IP_HEADERS.stream()
                .map(request::getHeader)
                .filter(xff -> !ObjectUtils.isEmpty(xff))
                .map(ipString -> ipString.split(",")[0].trim())
                .filter(ip -> isValidPublicIp(ip))
                .findFirst()
                .orElseGet(request::getRemoteAddr);
    }

    private static boolean isValidPublicIp(String ip) {
        return ip != null && !ip.equalsIgnoreCase("127.0.0.1")
                && !ip.equalsIgnoreCase("::1")
                && !ip.equalsIgnoreCase("unknown");
    }

    public static AuditLog build(Integer type, String operation) {
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        AuditLog auditLog = new AuditLog();
        auditLog.setCustomerInfo("");
        auditLog.setUserId(StpUtil.isLogin() ? StpUtil.getLoginIdAsString() : "");
        auditLog.setRequestUri(request.getRequestURI());
        auditLog.setRequestName("");
        auditLog.setRequestIp(getClientIp(request));
        auditLog.setRequestTime(DateTimeUtil.getCurrentTimeForFormat(DateTimeUtil.YMD_HMS));
        auditLog.setVersion(Constant.PRODUCT_VERSION);

        return auditLog;
    }

    public static void publish(String operation) {
        publish(TYPE_OK, operation);
    }

    public static void publish(int type, String operation) {
        AuditLog auditLog = AuditLogUtil.build(type, operation);
        SpringContextHolder.publishEvent(new AuditLogEvent(auditLog));
    }
}
