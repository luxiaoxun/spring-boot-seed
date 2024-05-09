package com.luxx.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommonUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getMd5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

}
