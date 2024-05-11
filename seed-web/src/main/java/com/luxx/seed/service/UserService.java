package com.luxx.seed.service;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.dao.RoleMapper;
import com.luxx.seed.dao.UserMapper;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.model.system.User;
import com.luxx.seed.model.system.UserRole;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.util.JsonUtil;
import com.luxx.seed.util.UserUtil;
import com.luxx.util.CommonUtil;
import com.luxx.util.PasswordUtil;
import com.luxx.util.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final static String key = "cdd936888e0a545c309cc82731c75efe";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    public User findByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    public List<Role> findRolesByUserId(Long userId) {
        return roleMapper.getRolesByUserId(userId);
    }

    public List<Menu> findMenusByRoleId(List<Long> roleIds) {
        return roleMapper.getMenusByRoleId(roleIds);
    }

    public List<User> getUsers(String username, Integer status, String order, String direction) {
        return userMapper.getUsers(username, status, order, direction);
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
            user.setCreateUser(UserUtil.getLoginUser());
            user.setUpdateUser(UserUtil.getLoginUser());
            Date now = new Date();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            userMapper.createUser(user);
            Long userId = user.getId();
            List<UserRole> userRoles = new ArrayList<>(user.getRoleIds().size());
            for (Long roleId : user.getRoleIds()) {
                userRoles.add(new UserRole(userId, roleId));
            }
            userMapper.insertUserRoles(userRoles);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create user error: " + ex);
            throw ex;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateUser(User user) {
        user.setTenantId(JsonUtil.encode(user.getTenantIds()));
        user.setUpdateUser(UserUtil.getLoginUser());
        user.setUpdateTime(new Date());
        Long userId = user.getId();
        List<UserRole> userRoles = new ArrayList<>(user.getRoleIds().size());
        for (Long roleId : user.getRoleIds()) {
            userRoles.add(new UserRole(userId, roleId));
        }
        userMapper.deleteUserRoles(userId);
        userMapper.insertUserRoles(userRoles);
        userMapper.updateUser(user);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteUser(Long id) {
        try {
            userMapper.deleteUserRoles(id);
            userMapper.deleteUser(id);
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
            userMapper.updateUserPassword(id, password, new Date());
            return ResponseUtil.success();
        } catch (Exception e) {
            log.error("Update user password error: " + e);
            return ResponseUtil.fail();
        }
    }

}
