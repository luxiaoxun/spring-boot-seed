package com.luxx.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class ParameterUtil {
    /**
     * @param reqJson     请求的参数，这里通常是JSON
     * @param excludeKeys 请求参数里面要去除哪些字段再求摘要
     * @return 去除参数的MD5摘要
     */
    public String paramMd5(final String reqJson, String... excludeKeys) {
        TreeMap paramTreeMap = JsonUtil.decode(reqJson, TreeMap.class);
        if (excludeKeys != null) {
            List<String> excludeKeyList = Arrays.asList(excludeKeys);
            if (!excludeKeyList.isEmpty()) {
                for (String excludeKey : excludeKeyList) {
                    paramTreeMap.remove(excludeKey);
                }
            }
        }
        String paramTreeMapJson = JsonUtil.encode(paramTreeMap);
        String paramMd5 = DigestUtils.md5DigestAsHex(paramTreeMapJson.getBytes(StandardCharsets.UTF_8));
        return paramMd5;
    }
}
