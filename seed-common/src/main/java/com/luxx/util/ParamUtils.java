package com.luxx.util;

import java.util.Date;

public class ParamUtils {
    public static <T> T getValue(T obj, T expect) {
        if (obj == null) {
            return expect;
        }
        return obj;
    }

    public static String getStringValue(String obj) {
        return getValue(obj, "");
    }

    public static Date getDateValue(Date obj) {
        return getValue(obj, new Date());
    }
}
