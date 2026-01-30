package com.luxx.seed.config.i18n;

public interface I18nEnum {
    /**
     * 获取枚举的国际化消息key
     */
    default String getMessageKey() {
        // 默认格式: enum.类名.枚举项名 (小写)
        String className = this.getClass().getSimpleName();
        String enumName = ((Enum<?>) this).name();
        return String.format("enum.%s.%s",
                className.toLowerCase(),
                enumName.toLowerCase());
    }
}
