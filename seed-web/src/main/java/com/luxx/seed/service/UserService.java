package com.luxx.seed.service;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.dao.UserMapper;
import com.luxx.seed.model.system.User;
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

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final static String key = "cdd93688-8e0a-545c-309c-c82731c75efe";

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers(String username, Integer status, String order, String direction) {
        return userMapper.getUsers(username, status, order, direction);
    }

    public Response createUser(User user) {
        try {
            user.setStatus(Status.ENABLED.getCode());
            //一个账号可以关联多个租户组织
            user.setTenantId(JsonUtil.encode(user.getTenantIds()));
            user.setCreateUser(UserUtil.getLoginUser());
            user.setUpdateUser(UserUtil.getLoginUser());
            Date now = new Date();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            userMapper.createUser(user);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create user error: " + ex);
            return ResponseUtil.fail();
        }
    }

    public boolean updateUser(User user) {
        user.setUpdateUser(UserUtil.getLoginUser());
        user.setUpdateTime(new Date());
        return userMapper.updateUser(user) > 0;
    }

    public boolean deleteUser(Long id) {
        return userMapper.updateUserStatus(id, Status.DELETED.getCode(), new Date()) > 0;
    }

    public Response updateUserPassword(Long id, String username, String password) {
        try {
            String decrypted = SecureUtil.decryptByAES(password, key);
            boolean valid = PasswordUtil.validatePassword(decrypted, username);
            if (!valid) {
                return ResponseUtil.fail(ResponseCode.PASSWORD_NOT_VALID);
            }
            password = CommonUtil.getMd5(decrypted);
            userMapper.updateUserPassword(id, password, new Date());
            return ResponseUtil.success();
        } catch (Exception e) {
            log.error("Update user password error: " + e);
            return ResponseUtil.fail();
        }
    }

}
