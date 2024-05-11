package com.luxx.seed.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.request.LoginRequest;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.model.system.User;
import com.luxx.seed.service.UserService;
import com.luxx.seed.service.AuthTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_INCORRECT);
        } else if (Status.LOCKED.getCode().equals(user.getStatus())) {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_LOCKED);
        }
        // check password
        if (!userService.checkPassword(user, password)) {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_INCORRECT);
        }
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("username", user.getUsername());
        // check user role
        List<Role> roles = userService.findRolesByUserId(user.getId());
        if (!CollectionUtils.isEmpty(roles)) {
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            loginInfo.put("roleNames", String.join(",", roleNames));
            List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            boolean isAdminRole = roleIds.contains(Constant.ADMIN_ROLE_ID);
            loginInfo.put("isAdminRole", isAdminRole);
            if (!isAdminRole) {
                List<Menu> menus = userService.findMenusByRoleId(roleIds);
                loginInfo.put("menus", menus);
            }
        } else {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_ILLEGAL);
        }
        // login
        StpUtil.login(user.getUsername());
//        StpUtil.getSession().set("user", user);
        loginInfo.put("token", StpUtil.getTokenInfo().getTokenValue());
        return ResponseUtil.success(loginInfo);
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    public Response logout() {
        log.info("User {} logout", StpUtil.getLoginId());
        StpUtil.logout();
        return ResponseUtil.success();
    }
}
