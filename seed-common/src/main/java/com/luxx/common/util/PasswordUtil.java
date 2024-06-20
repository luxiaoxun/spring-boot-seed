package com.luxx.common.util;

import java.util.regex.Pattern;

public class PasswordUtil {

    public static String generatePassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .build();
        String password = passwordGenerator.generate(8);
        return password;
    }

    //校验条件：最小8位数，最大32数，需包含数字、大小写字母和特殊字符中的一种（!@#$%&-_+.,），并且不能与用户名相同
    public static boolean validatePassword(String password, String username) {
        // 检查密码长度
        if (password.length() < 8 || password.length() > 32) {
            return false;
        }

        // 检查是否包含数字、大小写字母和特殊字符中的至少一种
        Pattern digitPattern = Pattern.compile("[0-9]");
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Pattern specialCharPattern = Pattern.compile("[!@#$%&\\-_+.,]");

        if (!digitPattern.matcher(password).find() ||
                !upperCasePattern.matcher(password).find() ||
                !lowerCasePattern.matcher(password).find() ||
                !specialCharPattern.matcher(password).find()) {
            return false;
        }

        // 检查是否与用户名相同
        if (password.equals(username)) {
            return false;
        }

        return true;
    }

}
