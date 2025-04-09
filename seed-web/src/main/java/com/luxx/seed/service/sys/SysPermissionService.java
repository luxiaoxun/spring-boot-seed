package com.luxx.seed.service.sys;

import cn.dev33.satoken.stp.StpInterface;
import com.luxx.seed.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysPermissionService implements StpInterface {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 如果是管理员admin，返回万能权限 *
        if (AuthUtil.isAdmin()) {
            return List.of("*");
        }
        return AuthUtil.getPermissionNames();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return AuthUtil.getRoleNames();
    }
}
