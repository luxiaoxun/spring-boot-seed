package com.luxx.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.luxx.model.config.Config;
import org.apache.commons.lang3.StringUtils;

public class ConfigUtils {
    public static Map<String, Object> getMap(List<Config> configs) {
        return getMap(configs, 0);
    }
    
    public static  Map<String, Object> getMap(List<Config> configs, int removePrefix) {
        Map<String, Object> map = new TreeMap<>();
        for (Config config : configs) {
            if (removePrefix == 0) {
                map.put(config.getName(), getValue(config));
            } else {
                if (config.getName().length() > removePrefix) {
                    map.put(config.getName().substring(removePrefix), getValue(config));
                }
            }
        }
        
        return map;
    }
    
    public static String getString(Config config) {
        return getString(config, null);
    }
    
    public static String getString(Config config, String defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        
        return config.getValue();
    }
    
    public static Object getValue(Config config) {
        if (config == null) {
            return null;
        }
        
        return convert(config.getValue(), config.getType(), config.getIsArray());
    }
    
    public static Object getValue(Config config, Object defaultValue) {
        if (config == null) {
            return defaultValue;
        }
        
        return convert(config.getValue(), config.getType(), config.getIsArray());
    }
    
    public static String[] getStringArrayValue(Config config) {
        if (config == null) {
            return new String[0];
        }
        
        Object value = getValue(config);
        
        if (value instanceof Object[]) {
            Object[] values = (Object[]) value;
            String[] ids = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                ids[i] = values[i].toString();
            }
            return ids;
        }
        
        return new String[]{config.getValue()};
    }
    
    public static Object convert(String value, String type, Boolean isArray) {
        if (Boolean.TRUE.equals(isArray)) {
            String[] values = value.split(",");
            List<Object> list = new ArrayList<>(values.length);
            for (String v : values) {
                if (StringUtils.isNotBlank(v)) {
                    list.add(convert(v, type));
                }
            }
            return list.toArray();
        } else {
            return convert(value, type);
        }
    }
    
    public static boolean validation(String value, String type, Boolean isArray) {
        try {
            convert(value, type, isArray);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static Object convert(String value, String type) {
        switch (type) {
        case "long":
            return Long.valueOf(value);
        case "double":
            return Double.valueOf(value);
        case "boolean":
            return Boolean.valueOf(value);
        default:
            return value;
        }
    }
}
