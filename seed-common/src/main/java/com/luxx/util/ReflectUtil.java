package com.luxx.util;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectUtil {
    private static final String EMPTY = "";

    public static <T> void setBeanNullProperty(List<T> list) {
        list.forEach(ReflectUtil::setBeanNullProperty);
    }

    public static <T> void setBeanNullProperty(T obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (field.getGenericType().getTypeName().equals("java.lang.String")) {
                    if (value == null) {
                        value = EMPTY;
                    }
                    // 去除空格
                    field.set(obj, value.toString().trim());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
