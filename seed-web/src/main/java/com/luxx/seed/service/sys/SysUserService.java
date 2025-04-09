package com.luxx.seed.service.sys;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.dao.SysRoleMapper;
import com.luxx.seed.dao.SysUserMapper;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.model.system.User;
import com.luxx.seed.model.system.UserRole;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.common.util.JsonUtil;
import com.luxx.seed.util.AuthUtil;
import com.luxx.common.util.CommonUtil;
import com.luxx.common.util.PasswordUtil;
import com.luxx.common.util.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class SysUserService {
    private final static String key = "cdd936888e0a545c309cc82731c75efe";

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    public User getByUsername(String username) {
        return sysUserMapper.getByUsername(username);
    }

    public List<Role> getRolesByUserId(Long userId) {
        return sysRoleMapper.getRolesByUserId(userId);
    }

    public List<Role> getRolesByUserName(String userName) {
        User user = sysUserMapper.getByUsername(userName);
        if (user != null) {
            return getRolesByUserId(user.getId());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Menu> getMenusByRoleId(List<Long> roleIds) {
        return sysRoleMapper.getMenusByRoleId(roleIds);
    }

    public List<User> getUsers(String username, Integer status, String order, String direction) {
        return sysUserMapper.getUsers(username, status, order, direction);
    }

    public void updateUserLoginStatus(User user, boolean loginStatus) {
        if (loginStatus) {
            user.setLoginAttempts(0);
            user.setLoginTime(new Date());
        }
        sysUserMapper.updateUser(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createUser(User user) throws Exception {
        try {
            String decrypted = SecureUtil.decryptByAES(user.getPassword(), key);
            boolean valid = PasswordUtil.validatePassword(decrypted, user.getUsername());
            if (!valid) {
                return ResponseUtil.fail(ResponseCode.PASSWORD_NOT_VALID);
            }
            String password = CommonUtil.getSha256(decrypted);
            user.setPassword(password);
            user.setStatus(Status.ENABLED.getCode());
            //一个账号可以关联多个租户组织
            user.setTenantId(JsonUtil.encode(user.getTenantIds()));
            user.setCreateUser(AuthUtil.getLoginUsername());
            user.setUpdateUser(AuthUtil.getLoginUsername());
            Date now = new Date();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            sysUserMapper.createUser(user);
            Long userId = user.getId();
            List<UserRole> userRoles = new ArrayList<>(user.getRoleIds().size());
            for (Long roleId : user.getRoleIds()) {
                userRoles.add(new UserRole(userId, roleId));
            }
            sysUserMapper.insertUserRoles(userRoles);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create user error: " + ex);
            throw ex;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateUser(User user) {
        user.setTenantId(JsonUtil.encode(user.getTenantIds()));
        user.setUpdateUser(AuthUtil.getLoginUsername());
        user.setUpdateTime(new Date());
        Long userId = user.getId();
        List<UserRole> userRoles = new ArrayList<>(user.getRoleIds().size());
        for (Long roleId : user.getRoleIds()) {
            userRoles.add(new UserRole(userId, roleId));
        }
        sysUserMapper.deleteUserRoles(userId);
        sysUserMapper.insertUserRoles(userRoles);
        sysUserMapper.updateUser(user);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteUser(Long id) {
        try {
            sysUserMapper.deleteUserRoles(id);
            sysUserMapper.deleteUser(id);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Delete user error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    public boolean checkPassword(User user, String password) {
        try {
            String decrypted = SecureUtil.decryptByAES(password, key);
            String passwordHash = CommonUtil.getSha256(decrypted);
            return passwordHash.equals(user.getPassword());
        } catch (Exception e) {
            log.error("Check password error: " + e);
            return false;
        }
    }

    public Response updateUserPassword(Long id, String username, String password) {
        try {
            String decrypted = SecureUtil.decryptByAES(password, key);
            boolean valid = PasswordUtil.validatePassword(decrypted, username);
            if (!valid) {
                return ResponseUtil.fail(ResponseCode.PASSWORD_NOT_VALID);
            }
            password = CommonUtil.getSha256(decrypted);
            User user = new User();
            user.setId(id);
            user.setPassword(password);
            user.setUpdateUser(AuthUtil.getLoginUsername());
            Date now = new Date();
            user.setUpdateTime(now);
            user.setPasswordUpdateTime(now);
            sysUserMapper.updateUser(user);
            return ResponseUtil.success();
        } catch (Exception e) {
            log.error("Update user password error: " + e);
            return ResponseUtil.fail();
        }
    }

    public List<User> findLockedUsers() {
        return sysUserMapper.getUsers(null, Status.LOCKED.getCode(), null, null);
    }

    public void updateUserLockedStatus(List<User> users) {
        sysUserMapper.batchUpdateUsers(users);
    }

}
