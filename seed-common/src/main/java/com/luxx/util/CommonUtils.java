package com.luxx.util;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.RandomStringUtils;

public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static String getIPS() {

        Enumeration<NetworkInterface> b;
        try {
            b = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            String random = "random-" + System.currentTimeMillis() + "-" + RandomStringUtils.randomAlphanumeric(8);
            logger.error("Fail to get IPs. Use string [ " + random + " ] as IP.", e);
            return random;
        }

        StringBuilder stringBuilder = new StringBuilder();
        while (b.hasMoreElements()) {
            for (InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                if (!f.getAddress().isLoopbackAddress() && f.getAddress() instanceof Inet4Address) {
                    stringBuilder.append(f.getAddress().getHostAddress());
                    stringBuilder.append(";");
                }
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public static boolean arrayEquals(String[] array1, String[] array2) {
        Set<String> set1 = new HashSet<>(Arrays.asList(array1));
        Set<String> set2 = new HashSet<>(Arrays.asList(array2));

        return set1.size() == set2.size() && set1.containsAll(set2);
    }

    public static List<String> toString(List<?> list) {
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static List<Long> toLong(List<String> list) {
        return list.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public static Set<Long> toLong(Set<String> set) {
        return set.stream().map(Long::parseLong).collect(Collectors.toSet());
    }

    public static List<Integer> toInt(List<String> list) {
        return list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

}
