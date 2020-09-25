package com.luxx.seed.controller.login;

import com.luxx.seed.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "login")
@Slf4j
public class LoginController extends BaseController {

    @ApiOperation(value = "login", notes = "login")
    @PostMapping("/login")
    public void login(@RequestParam String username, @RequestParam String password) {
        log.info("User login: " + username);
        // check user info
        setUserInfoInSession(username);
    }

    @ApiOperation(value = "logout", notes = "logout")
    @PostMapping("/logout")
    public void logout(@RequestParam String username) {
        log.info("User logout: " + username);
        // check user info
        removeUserInfoInSession();
    }
}
