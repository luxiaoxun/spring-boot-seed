package com.luxx.common.util.test;

import com.luxx.common.util.SecureUtil;
import org.junit.Test;

public class SecureUtilTest {
    @Test
    public void testAes() {
        //16位密码
        String key = "1234567890abcdef";
        String content = "I am luxiaoxun 中国人";
        try {
            String encryptStr = SecureUtil.encryptByAES(content, key);
            System.out.println(encryptStr);
            String str = SecureUtil.decryptByAES(encryptStr, key);
            System.out.println(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testRSA() {
        String content = "I am luxiaoxun 中国人";
        try {
//            SecureUtil.genKeyPair();
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF6taq2fmwKZCageHwNwQlelTAsq67JhAxRQZVX0aDNVZiBt86V1GgpwlhoO83zl/coZUxnkJP9NKaKVUvrxF7mRZnKkSivBmJIzdQ/gl3kpwjMSZhzPB7Bmrjtp6w3YIxO+07q/L3oKMLY0WHP/xNHNqeYg/26MVuKIVab1fYZwIDAQAB";
            String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIXq1qrZ+bApkJqB4fA3BCV6VMCyrrsmEDFFBlVfRoM1VmIG3zpXUaCnCWGg7zfOX9yhlTGeQk/00popVS+vEXuZFmcqRKK8GYkjN1D+CXeSnCMxJmHM8HsGauO2nrDdgjE77Tur8vegowtjRYc//E0c2p5iD/boxW4ohVpvV9hnAgMBAAECgYBlqAf+t+rffB3l+9RcOhcur/coNDuJm3JNRF1zbxiiv1lsnlTT/7baIhJwBu+wyDtiy1Hq0Xz7HxjDp76frOCyfWO4kzywYHEe/xd+J4pbrFFCNxl+t6F+nfs5V6uZSydrX2YaXfbu4iZCqUW383S0WZ5Hf5KrVuFxFCBRvJYamQJBAMgtH9m/Dz6NartPvGdMghmmRjm1jIsUvJD5y6JHIpEL8gVjmSkYX97KlrzgTWyYPrwxwY1YIlJb8Famn1ZnPX0CQQCrQ2R/LCtWjgfgVx11HiWo5Oj4TobW2teZTcwQ1oeJz96CI0PL7vX2JvTQgcdplDD3WNA+t1ow7AVXuKxIdmKzAkAyUXWCLM7OMIo1z5NSfB7wB6X7d7F+fJXx83jGsA72WwRZanHjrKbcwBwoGlIcDCEo+XDn8NGf1lCR14ySEZ2ZAkEAiV/4h84NQVOSW9BlD6HMAYsEmNCcdlF6aT0vOD5s4A1+ZRMWecnHP9iFvY6sPzMgh/eUqwn17ZIUwwWeJE3Z4wJBAKHnrdRrNIS/CF/uKNBScqymjz3MsklaxAL9gWz6dTqNzSEMFaGS5AyBM4OTWN7Tu/mfhBknNtVRNm11sbj9HEE=";
            String encryptStr = SecureUtil.encryptByPublicKey(content, publicKey);
            System.out.println(encryptStr);
            String str = SecureUtil.decryptByPrivateKey(encryptStr, privateKey);
            System.out.println(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
