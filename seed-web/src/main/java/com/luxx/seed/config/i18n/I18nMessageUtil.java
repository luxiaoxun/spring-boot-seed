package com.luxx.seed.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
        Locale locale = getCurrentLocal();
        return messageSource.getMessage(msgKey, null, locale);
    }

    /**
     * 获取枚举的国际化显示名称
     */
    public static String getEnumMsg(Enum<?> enumValue) {
        if (enumValue == null) {
            return "";
        }
        String messageKey = getEnumMessageKey(enumValue);
        Locale locale = getCurrentLocal();
        return messageSource.getMessage(messageKey, null, locale);
    }

    /**
     * 生成枚举的消息key
     */
    private static String getEnumMessageKey(Enum<?> enumValue) {
        // 如果枚举实现了I18nEnum接口，使用其自定义的key
        if (enumValue instanceof I18nEnum) {
            return ((I18nEnum) enumValue).getMessageKey();
        }

        // 默认使用类名
        String className = enumValue.getClass().getSimpleName();
        return String.format("enum.%s.%s",
                className.toLowerCase(),
                enumValue.name().toLowerCase());
    }
}
