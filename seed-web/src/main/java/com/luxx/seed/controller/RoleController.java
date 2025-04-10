package com.luxx.seed.controller;

import com.luxx.seed.model.system.Role;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.sys.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
@Tag(name = "role")
@Slf4j
public class RoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary = "查询role")
    @GetMapping("/list")
    public Response getRoles() {
        return ResponseUtil.success(sysRoleService.getRoles());
    }

    @Operation(summary = "创建role")
    @PostMapping("/create")
    public Response createRole(@RequestBody Role role) {
        try {
            if (ObjectUtils.isEmpty(role.getName()) || CollectionUtils.isEmpty(role.getMenuIds())) {
                return ResponseUtil.fail(ResponseCode.ROLE_NOT_VALID);
            }
            return sysRoleService.createRole(role);
        } catch (Exception ex) {
            log.error("Create role error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "更新role")
    @PostMapping("/update")
    public Response updateRole(@RequestBody Role role) {
        try {
            if (ObjectUtils.isEmpty(role.getName()) || CollectionUtils.isEmpty(role.getMenuIds())) {
                return ResponseUtil.fail(ResponseCode.ROLE_NOT_VALID);
            }
            return sysRoleService.updateRole(role);
        } catch (Exception ex) {
            log.error("Update role error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "删除role")
    @PostMapping("/delete")
    public Response deleteRole(@RequestParam Long id) {
        try {
            return sysRoleService.deleteRole(id);
        } catch (Exception ex) {
            log.error("Delete role error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "菜单列表")
    @GetMapping("/menus")
    public Response getMenus() {
        return ResponseUtil.success(sysRoleService.getMenuTreeList(0L));
    }

}
