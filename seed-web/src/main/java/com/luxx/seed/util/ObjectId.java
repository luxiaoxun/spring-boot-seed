package com.luxx.seed.util;

import org.apache.logging.log4j.core.util.UuidUtil;

import java.util.UUID;

public class ObjectId {
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuid() {
        //It is guaranteed to be unique for about 8,900 years
        //so long as you generate less than 10,000 UUIDs per millisecond.
        return UuidUtil.getTimeBasedUuid().toString().replace("-", "");
    }

}
