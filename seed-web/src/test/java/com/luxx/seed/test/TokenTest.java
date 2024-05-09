package com.luxx.seed.test;

import com.luxx.seed.model.system.User;
import com.luxx.seed.service.AuthTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@Configuration
@SpringBootTest(classes = {AuthTokenService.class})
public class TokenTest {

    @Autowired
    AuthTokenService authTokenService;

    @Test
    public void testToken() {
        User user = new User();
        user.setId(1L);
        user.setPassword("123456");
        user.setUsername("luxiaoxun");

        System.out.println(authTokenService.getToken(user));
    }

    @Test
    public void testDecodeToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoMCIsImF1ZCI6WyIxMjMiLCJkZWZhdWx0LWRldmljZSJdLCJleHAiOjE3MDYxNzcxNzAsImV4dHJhTWFwIjpudWxsfQ.9XlDRAiU6TdrRh-H3EhQvSrnDBfmEqkY2BhSaprlqvE";
        System.out.println(authTokenService.verifyToken(token));
    }

}
