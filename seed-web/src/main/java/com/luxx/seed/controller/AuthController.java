package com.luxx.seed.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.model.auth.UserInfo;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.request.LoginRequest;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.model.system.User;
import com.luxx.seed.service.sys.SysUserService;
import com.luxx.seed.util.AuditLogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth")
@Slf4j
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/login/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        String captchaText = captchaProducer.createText();
        session.setAttribute("captcha", captchaText);
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        try (OutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(captchaImage, "jpg", outputStream);
        }
    }

    @Operation(summary = "login")
    @PostMapping("/login")
    public Response login(@Validated @RequestBody LoginRequest request, HttpSession session) {
        log.info("User {} try to login", request.getUsername());
        // check captcha
        String captcha = (String) session.getAttribute("captcha");
        String verifyText = request.getVerifyText();
        if (ObjectUtils.isEmpty(verifyText) || !verifyText.equalsIgnoreCase(captcha)) {
            return ResponseUtil.fail(ResponseCode.AUTH_CAPTCHA_ERROR);
        }

        String username = request.getUsername();
        String password = request.getPassword();
        // check user info
        User user = sysUserService.getByUsername(username);
        if (user == null) {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_INCORRECT);
        } else if (Status.LOCKED.getCode().equals(user.getStatus())) {
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_LOCKED);
        }
        // check password
        if (!sysUserService.checkPassword(user, password)) {
            if (user.getLoginAttempts() >= 5) {
                user.setStatus(Status.LOCKED.getCode());
                user.setLockedTime(new Date());
            } else {
                user.setLoginAttempts(user.getLoginAttempts() + 1);
            }
            sysUserService.updateUserLoginStatus(user, false);
            AuditLogUtil.publishFail(username, "登录失败");
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_INCORRECT);
        }

        // check user role
        List<Role> roles = sysUserService.getRolesByUserId(user.getId());
        if (CollectionUtils.isEmpty(roles)) {
            AuditLogUtil.publishFail(username, "登录失败");
            return ResponseUtil.fail(ResponseCode.AUTH_ACCOUNT_ILLEGAL);
        }

        UserInfo userInfo = getLoginUserInfo(user, roles);
        // login
        StpUtil.login(user.getUsername());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        StpUtil.getSession().set(Constant.AUTH_USER_INFO_KEY, userInfo)
                .set(Constant.AUTH_TOKEN_INFO_KEY, tokenInfo);
        userInfo.setToken(tokenInfo.getTokenValue());
        //Update login status
        sysUserService.updateUserLoginStatus(user, true);
        AuditLogUtil.publishOk("登录成功");
        return ResponseUtil.success(userInfo);
    }

    private UserInfo getLoginUserInfo(User user, List<Role> roles) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRoles(roles);
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        boolean isAdmin = roleIds.contains(Constant.ADMIN_ROLE_ID);
        userInfo.setAdmin(isAdmin);
        if (!isAdmin) {
            List<Menu> menus = sysUserService.getMenusByRoleId(roleIds);
            userInfo.setMenus(menus);
        }
        return userInfo;
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    public Response logout() {
        log.info("User {} logout", StpUtil.getLoginId());
        StpUtil.logout();
        AuditLogUtil.publishOk("退出系统");
        return ResponseUtil.success();
    }
}
