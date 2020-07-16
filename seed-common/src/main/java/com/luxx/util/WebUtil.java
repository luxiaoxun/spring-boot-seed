package com.luxx.util;

import cn.hutool.core.lang.ObjectId;

public class WebUtil {

    public static final String REQ_ID_HEADER = "Request-Id";

    private static final ThreadLocal<String> reqIdThreadLocal = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        reqIdThreadLocal.set(requestId);
    }

    public static String getRequestId() {
        String requestId = reqIdThreadLocal.get();
        if (requestId == null) {
            requestId = ObjectId.next();
            reqIdThreadLocal.set(requestId);
        }
        return requestId;
    }

    public static void removeRequestId() {
        reqIdThreadLocal.remove();
    }
}
