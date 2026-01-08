package com.luxx.seed.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nMessageUtil {
    private static MessageSource messageSource;

    public I18nMessageUtil(MessageSource messageSource) {
        I18nMessageUtil.messageSource = messageSource;
    }

    public static Locale getCurrentLocal() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale == null) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        return locale;
    }

    public static String getMsg(String msgKey) {
        Locale defaultLocale = getCurrentLocal();
        return messageSource.getMessage(msgKey, null, defaultLocale);
    }

    public static String getMsg(String msgKey, @Nullable Object[] args) {
        Locale defaultLocale = getCurrentLocal();
        return messageSource.getMessage(msgKey, args, defaultLocale);
    }
}
