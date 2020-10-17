package com.luxx.seed.controller;

import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.model.User;
import com.luxx.seed.service.UserService;
import com.luxx.seed.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "login")
@Slf4j
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "login", notes = "login")
    @PostMapping("/login")
    public Response login(@RequestParam String username, @RequestParam String password) {
        log.info("User try to login: " + username);
        // check user info
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseUtil.fail(ResponseCode.LOGIN_FAILED);
        } else {
            if (!user.getPassword().equals(password)) {
                return ResponseUtil.fail(ResponseCode.LOGIN_FAILED);
            } else {
                String token = TokenUtil.getToken(user);
                return ResponseUtil.success(token);
            }
        }
    }

    @ApiOperation(value = "logout", notes = "logout")
    @PostMapping("/logout")
    public void logout(@RequestParam String username) {
        log.info("User logout: " + username);
        // check user info
        removeUserInfo();
    }
}
