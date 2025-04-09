package com.luxx.seed.util;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.exception.AuthException;
import com.luxx.seed.model.auth.UserInfo;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;

import java.util.ArrayList;
import java.util.List;

public class AuthUtil {

    public static boolean isAdmin() {
        UserInfo userInfo = getUserInfo();
        if (userInfo == null || userInfo.getRoles() == null) {
            return false;
        }
        return userInfo.getRoles().stream().anyMatch(role -> role.getId().equals(Constant.ADMIN_ROLE_ID));
    }

    public static String getLoginUsername() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 获取用户数据
     */
    public static UserInfo getUserInfo() {
        try {
            return (UserInfo) StpUtil.getSession().get(Constant.AUTH_USER_INFO_KEY);
        } catch (Exception e) {
            throw new AuthException("403", "登录已失效，请重新登陆");
        }
    }

    /**
     * 获取用户角色集合
     */
    public static List<String> getRoleNames() {
        UserInfo userInfo = getUserInfo();
        if (userInfo == null || userInfo.getRoles() == null) {
            return new ArrayList<>();
        }
        return userInfo.getRoles().stream().map(Role::getName).toList();
    }

    /**
     * 获取权限集合
     */
    public static List<String> getPermissionNames() {
        UserInfo userInfo = getUserInfo();
        if (userInfo == null || userInfo.getMenus() == null) {
            return new ArrayList<>();
        }
        return userInfo.getMenus().stream().map(Menu::getCode).toList();
    }


}
