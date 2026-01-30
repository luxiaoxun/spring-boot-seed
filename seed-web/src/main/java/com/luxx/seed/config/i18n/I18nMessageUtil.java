package com.luxx.seed.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class I18nMessageUtil {
    private static MessageSource messageSource;

    // 缓存枚举类的前缀配置
    private static final Map<Class<? extends Enum<?>>, String> ENUM_PREFIX_CACHE = new ConcurrentHashMap<>();

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

    /**
     * 获取枚举的国际化显示名称
     */
    public static String getEnumMsg(Enum<?> enumValue) {
        if (enumValue == null) {
            return "";
        }

        String messageKey = getEnumMessageKey(enumValue);
        Locale locale = getCurrentLocal();

        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (Exception e) {
            // 如果没有找到国际化消息，返回枚举名称或原始name（如果有）
            return getEnumFallbackName(enumValue);
        }
    }

    /**
     * 生成枚举的消息key
     */
    private static String getEnumMessageKey(Enum<?> enumValue) {
        // 如果枚举实现了I18nEnum接口，使用其自定义的key
        if (enumValue instanceof I18nEnum) {
            return ((I18nEnum) enumValue).getMessageKey();
        }

        // 否则使用默认格式
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) enumValue.getClass();
        // 默认使用类名
        String prefix = ENUM_PREFIX_CACHE.computeIfAbsent(enumClass, Class::getSimpleName);

        return String.format("enum.%s.%s",
                prefix.toLowerCase(),
                enumValue.name().toLowerCase().replace("_", ""));
    }

    /**
     * 获取枚举的备用名称（当没有国际化消息时使用）
     */
    private static String getEnumFallbackName(Enum<?> enumValue) {
        // 优先尝试获取name字段
        try {
            Method getNameMethod = enumValue.getClass().getMethod("getName");
            if (getNameMethod != null) {
                Object nameValue = getNameMethod.invoke(enumValue);
                if (nameValue != null) {
                    return nameValue.toString();
                }
            }
        } catch (Exception e) {
            // 没有getName方法，继续尝试其他方式
        }

        // 尝试获取desc或description字段
        try {
            Method getDescMethod = enumValue.getClass().getMethod("getDesc");
            if (getDescMethod != null) {
                Object descValue = getDescMethod.invoke(enumValue);
                if (descValue != null) {
                    return descValue.toString();
                }
            }
        } catch (Exception e) {
            // 没有getDesc方法
        }

        try {
            Method getDescriptionMethod = enumValue.getClass().getMethod("getDescription");
            if (getDescriptionMethod != null) {
                Object descValue = getDescriptionMethod.invoke(enumValue);
                if (descValue != null) {
                    return descValue.toString();
                }
            }
        } catch (Exception e) {
            // 没有getDescription方法
        }

        // 最后返回枚举名称
        return enumValue.name();
    }
}
