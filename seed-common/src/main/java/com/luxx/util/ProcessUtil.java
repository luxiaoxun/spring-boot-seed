package com.luxx.util;

import java.lang.management.ManagementFactory;

public class ProcessUtil {
    public static int getCurrentPID() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(name.split("@")[0]);
    }
}
