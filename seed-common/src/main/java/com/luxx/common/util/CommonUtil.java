package com.luxx.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static String getMd5(String input) {
        try {
            // 创建MessageDigest对象，指定算法为MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // 将字符串转换为字节数组，并计算哈希值
            byte[] hashBytes = digest.digest(input.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
    }

    public static String getSha256(String input) {
        try {
            // 创建MessageDigest对象，指定算法为SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 将字符串转换为字节数组，并计算哈希值
            byte[] hashBytes = digest.digest(input.getBytes());
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
    }
}
