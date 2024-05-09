package com.luxx.seed.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.system.User;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.UserService;
import com.luxx.seed.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询user")
    @GetMapping("/page-list")
    public Response getUsersByPage(@RequestParam(required = false) String username,
                                   @RequestParam(required = false) int status,
                                   @RequestParam(defaultValue = "id") String order,
                                   @RequestParam(defaultValue = "ASC") String direction,
                                   @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        log.info("{} get user by page", UserUtil.getLoginUser());
        if (!Constant.SORT_ASC.equals(direction) && !Constant.SORT_DESC.equals(direction)) {
            return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.getUsers(username, status, order, direction);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("data", pageInfo.getList());
        return ResponseUtil.success(map);
    }

    @Operation(summary = "创建user")
    @PostMapping("/create")
    public Response createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "更新user")
    @PostMapping("/update")
    public Response updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Update user error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "删除user")
    @PostMapping("/delete")
    public Response deleteUser(@RequestParam Long id) {
        try {
            userService.deleteUser(id);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Delete user error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Response changePassword(@RequestParam Long id,
                                   @RequestParam String username,
                                   @RequestParam String password) {
        return userService.updateUserPassword(id, username, password);
    }

}
