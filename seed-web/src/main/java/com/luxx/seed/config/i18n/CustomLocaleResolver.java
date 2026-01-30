package com.luxx.seed.config.i18n;

import com.luxx.seed.service.sys.SysUserService;
import com.luxx.seed.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Slf4j
public class CustomLocaleResolver implements LocaleResolver {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 1. 首先检查请求参数（优先级最高）
        String langParam = request.getParameter("lang");
        if (langParam != null && !langParam.isEmpty()) {
            try {
                return parseLocale(langParam);
            } catch (Exception e) {
                log.warn("Can not parse local language: {}, use default", langParam);
            }
        }

        // 2. 检查当前用户的语言设置（从数据库获取）
        try {
            String username = AuthUtil.getLoginUsername();
            String localLanguage = sysUserService.getLocalLanguage(username);
            if (localLanguage != null && !localLanguage.trim().isEmpty()) {
                Locale dbLocale = parseLocale(localLanguage);
                log.debug("Use database local language: {}", dbLocale);
                return dbLocale;
            }
        } catch (Exception e) {
            log.error("Get local language from database error", e);
        }

        // 3. 检查Accept-Language头
        String acceptLanguage = request.getHeader("Accept-Language");
        if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
            Locale requestLocale = request.getLocale();
            if (requestLocale != null) {
                log.debug("Use Accept-Language header: {}", requestLocale);
                return requestLocale;
            }
        }

        return Locale.SIMPLIFIED_CHINESE;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // 设置会话中的语言
        if (locale != null) {
            request.getSession().setAttribute("SESSION_LOCALE", locale);
        } else {
            request.getSession().removeAttribute("SESSION_LOCALE");
        }
    }

    /**
     * 解析语言字符串为Locale对象
     * 支持格式：zh_CN, zh-CN, en_US, en-US, zh, en等
     *
     * @param languageTag 语言标签字符串
     * @return Locale对象
     */
    private Locale parseLocale(String languageTag) {
        if (languageTag == null || languageTag.trim().isEmpty()) {
            return Locale.SIMPLIFIED_CHINESE;
        }

        String normalizedTag = languageTag.trim();

        // 处理下划线分隔符（zh_CN）
        if (normalizedTag.contains("_")) {
            String[] parts = normalizedTag.split("_");
            if (parts.length >= 2) {
                return new Locale(parts[0], parts[1]);
            } else {
                return new Locale(parts[0]);
            }
        }

        // 处理连字符分隔符（zh-CN）
        if (normalizedTag.contains("-")) {
            String[] parts = normalizedTag.split("-");
            if (parts.length >= 2) {
                return new Locale(parts[0], parts[1]);
            } else {
                return new Locale(parts[0]);
            }
        }

        // 简单语言代码（zh, en）
        return new Locale(normalizedTag);
    }
}