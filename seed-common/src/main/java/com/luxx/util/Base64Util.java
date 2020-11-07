package com.luxx.util;

import java.util.Base64;

public class Base64Util {

    public static String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str));
    }

    public static String base64UrlEncode(String base64Str) {
        return base64Str.replace('+', '.')
                .replace('/', '_')
                .replace('=', '-');
    }

    public static String base64UrlDecode(String base64Str) {
        return base64Str.replace('.', '+')
                .replace('_', '/')
                .replace('-', '=');
    }

}
