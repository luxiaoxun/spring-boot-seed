package com.luxx.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonUtil {
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

    public static String getMd5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    private static final String BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        int targetBase = BASE.length();
        do {
            int i = (int) (num % targetBase);
            sb.append(BASE.charAt(i));
            num /= targetBase;
        } while (num > 0);
        return sb.reverse().toString();
    }

    public static long toBase10(String input) {
        int srcBase = BASE.length();
        long id = 0;
        String r = new StringBuilder(input).reverse().toString();
        for (int i = 0; i < r.length(); i++) {
            int charIndex = BASE.indexOf(r.charAt(i));
            id += charIndex * (long) Math.pow(srcBase, i);
        }
        return id;
    }

}
