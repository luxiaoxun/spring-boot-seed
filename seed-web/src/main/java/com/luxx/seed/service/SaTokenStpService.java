package com.luxx.seed.service;

import cn.dev33.satoken.stp.StpInterface;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaTokenStpService implements StpInterface {

    @Autowired
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = new ArrayList<>();
        List<Role> roles = userService.getRolesByUserName(loginId.toString());
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<Menu> menus = userService.getMenusByRoleId(roleIds);
        //菜单页面权限
        permissionList = menus.stream().map(Menu::getCode).collect(Collectors.toList());
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = new ArrayList<>();
        List<Role> roles = userService.getRolesByUserName(loginId.toString());
        roleList = roles.stream().map(Role::getName).collect(Collectors.toList());
        return roleList;
    }
}
