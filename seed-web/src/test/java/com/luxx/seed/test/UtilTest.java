package com.luxx.seed.test;

import com.luxx.common.util.CommonUtil;
import com.luxx.common.util.SecureUtil;
import org.junit.Test;

public class UtilTest {
    @Test
    public void test() {
        String key = "cdd936888e0a545c309cc82731c75efe";
        try {
            String str = "123456";
            System.out.println(SecureUtil.encryptByAES(str, key)); //GUoL+tCg0AAiYDZOOdLryg==
            System.out.println(CommonUtil.getSha256(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
