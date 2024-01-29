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

    public static String getMsg(String msgKey) {
        Locale defaultLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgKey, null, defaultLocale);
    }

    public static String getMsg(String msgKey, @Nullable Object[] args) {
        Locale defaultLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgKey, args, defaultLocale);
    }

    /**
     * 指定语言获得单个国际化翻译值
     *
     * @param msgKey   国际化文件key值
     * @param language 语言，格式 zh_CN
     * @param args     其他参数，可为null
     */
    public static String getMsgByLanguage(String msgKey, String language, Object... args) {
        String[] localeArgs = language.split("_");
        Locale locale = new Locale(localeArgs[0], localeArgs[1]);
        return messageSource.getMessage(msgKey, args, locale);
    }
}
