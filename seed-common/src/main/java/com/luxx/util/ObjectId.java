package com.luxx.util;

import org.apache.logging.log4j.core.util.UuidUtil;

import java.security.SecureRandom;
import java.util.UUID;

public class ObjectId {

    // Maxim: Copied from UUID implementation :)
    private static volatile SecureRandom numberGenerator = null;
    private static final long MSB = 0x8000000000000000L;

    public static String uniqueId() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }
        return Long.toHexString(MSB | ng.nextLong()) + Long.toHexString(MSB | ng.nextLong());
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuid() {
        //It is guaranteed to be unique for about 8,900 years
        //so long as you generate less than 10,000 UUIDs per millisecond.
        return UuidUtil.getTimeBasedUuid().toString().replace("-", "");
    }
}
