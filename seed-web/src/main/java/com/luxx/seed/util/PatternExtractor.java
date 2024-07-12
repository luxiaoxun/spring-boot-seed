package com.luxx.seed.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternExtractor {
    public static final Pattern MAIL = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern URL = Pattern.compile("^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))([-%()_.!~*';/?:@&=+$,A-Za-z0-9])+)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern SSN = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
    public static final Pattern UUID = Pattern.compile("^[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern CHINA_PHONE_NUMBER = Pattern.compile("^1[3-9]\\d{9}$");

    public static String extractPhoneNumber(String text) {
        Matcher matcher = CHINA_PHONE_NUMBER.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String extractEmail(String text) {
        Matcher matcher = MAIL.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String extractURL(String text) {
        Matcher matcher = URL.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String extractSSN(String text) {
        Matcher matcher = SSN.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String extractUUID(String text) {
        Matcher matcher = UUID.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
