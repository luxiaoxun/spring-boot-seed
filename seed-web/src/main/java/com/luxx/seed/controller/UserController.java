package com.luxx.seed.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.system.User;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.sys.SysUserService;
import com.luxx.seed.util.AuthUtil;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/user")
@Slf4j
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/page-list")
    public Response getUsersByPage(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam(defaultValue = "id") String order,
                                   @RequestParam(defaultValue = "ASC") String direction,
                                   @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        log.info("{} get user by page", AuthUtil.getLoginUsername());
        if (!Constant.SORT_ASC.equals(direction) && !Constant.SORT_DESC.equals(direction)) {
            return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = sysUserService.getUsers(username, status, order, direction);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("data", pageInfo.getList());
        return ResponseUtil.success(map);
    }

    @PostMapping("/create")
    public Response createUser(@RequestBody User user) {
        if (ObjectUtils.isEmpty(user.getUsername()) || ObjectUtils.isEmpty(user.getPassword())
                || CollectionUtils.isEmpty(user.getTenantIds()) || CollectionUtils.isEmpty(user.getRoleIds())) {
            return ResponseUtil.fail(ResponseCode.ACCOUNT_NOT_VALID);
        }
        try {
            return sysUserService.createUser(user);
        } catch (Exception ex) {
            log.error("Create user error: " + ex);
            return ResponseUtil.fail();
        }
    }

    @PostMapping("/update")
    public Response updateUser(@RequestBody User user) {
        if (CollectionUtils.isEmpty(user.getTenantIds()) || CollectionUtils.isEmpty(user.getRoleIds())) {
            return ResponseUtil.fail(ResponseCode.ACCOUNT_NOT_VALID);
        }
        try {
            return sysUserService.updateUser(user);
        } catch (Exception ex) {
            log.error("Update user error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @PostMapping("/delete")
    public Response deleteUser(@RequestParam Long id) {
        return sysUserService.deleteUser(id);
    }

    @PostMapping("/password")
    public Response changePassword(@RequestParam Long id,
                                   @RequestParam String username,
                                   @RequestParam String password) {
        return sysUserService.updateUserPassword(id, username, password);
    }

}
