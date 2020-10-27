package com.luxx.seed.test;


import com.luxx.seed.model.User;
import com.luxx.seed.util.TokenUtil;
import org.junit.Test;

public class TokenTest {
    @Test
    public void testToken() {
        User user = new User();
        user.setId(1);
        user.setPassword("123");
        user.setUsername("luxiaoxun");

        System.out.println(TokenUtil.getToken(user));
    }

}
