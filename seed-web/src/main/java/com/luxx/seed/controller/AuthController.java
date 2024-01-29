package com.luxx.seed.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.request.LoginRequest;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.model.User;
import com.luxx.seed.service.UserService;
import com.luxx.seed.service.AuthTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;

    @Operation(summary = "login")
    @PostMapping("/login")
    public Response login(@Validated @RequestBody LoginRequest request) {
        log.info("User {} try to login", request.getUsername());
        String username = request.getUsername();
        String password = request.getPassword();
        // check user info
        if (username.equals("admin") && password.equals("123456")) {
            User user = new User();
            user.setId(123L);
            user.setUsername(username);
            user.setPassword(password);
            Object loginInfo = doLogin(user);
            return ResponseUtil.success(loginInfo);
        }
        return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_INCORRECT);
    }

    private Object doLogin(User user) {
        StpUtil.login(user.getId());
        StpUtil.getSession().set("user", user);
        return StpUtil.getTokenInfo();
    }

    @Operation(summary = "login-session")
    @PostMapping("/login-session")
    public Response loginSession() {
        return ResponseUtil.success(StpUtil.getSession());
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    public Response logout() {
        log.info("User {} logout", StpUtil.getLoginId());
        StpUtil.logout();
        return ResponseUtil.success();
    }
}
