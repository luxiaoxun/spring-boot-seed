package com.luxx.util;

import java.util.concurrent.ConcurrentHashMap;

public class Md5CodeUtils {
    private static Md5CodeUtils md5CodeUtils;

    private ConcurrentHashMap<String, String> map;

    private Md5CodeUtils() {
        map = new ConcurrentHashMap<>();
    }

    public static Md5CodeUtils getMd5CodeUtils() {
        if (md5CodeUtils == null) {
            md5CodeUtils = new Md5CodeUtils();
        }
        return md5CodeUtils;
    }

    public synchronized void addmd5CodeUtils(String phone, String openIdAndKey) {
        map.put(phone, openIdAndKey);
    }

    public synchronized void delmd5CodeUtils(String phone) {
        map.remove(phone);
    }

    public synchronized String getmd5Code(String phone) {
        if (phone == null) {
            return null;
        }
        return map.get(phone);
    }

}
