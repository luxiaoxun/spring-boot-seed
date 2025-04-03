package com.luxx.seed.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxx.seed.constant.Constant;
import com.luxx.seed.model.system.Tenant;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseCode;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.service.sys.SysTenantService;
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
@RequestMapping("/system/tenant")
@Tag(name = "tenant")
@Slf4j
public class TenantController {

    @Autowired
    private SysTenantService sysTenantService;

    @Operation(summary = "查询tenant")
    @GetMapping("/list")
    public Response getTenants() {
        log.info("{} get all tenants", UserUtil.getLoginUsername());
        List<Tenant> tenantList = sysTenantService.getAllTenants();
        return ResponseUtil.success(tenantList);
    }

    @Operation(summary = "分页查询tenant")
    @GetMapping("/page-list")
    public Response getTenantsByPage(@RequestParam(defaultValue = "id") String order,
                                     @RequestParam(defaultValue = "ASC") String direction,
                                     @RequestParam(defaultValue = "1") @Min(1) int pageNum,
                                     @RequestParam(defaultValue = "10") int pageSize) {
        log.info("{} get tenant by page", UserUtil.getLoginUsername());
        if (!Constant.SORT_ASC.equals(direction) && !Constant.SORT_DESC.equals(direction)) {
            return ResponseUtil.fail(ResponseCode.PARAM_ERROR);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Tenant> tenantList = sysTenantService.getTenants(order, direction);
        PageInfo<Tenant> pageInfo = new PageInfo<>(tenantList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("data", pageInfo.getList());
        return ResponseUtil.success(map);
    }

    @Operation(summary = "创建tenant")
    @PostMapping("/create")
    public Response createTenant(@RequestBody Tenant tenant) {
        try {
            sysTenantService.createTenant(tenant);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Create tenant error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

    @Operation(summary = "删除tenant")
    @PostMapping("/delete")
    public Response deleteTenant(@RequestParam String id) {
        try {
            sysTenantService.deleteTenant(id);
            return ResponseUtil.success();
        } catch (Exception ex) {
            log.error("Delete tenant error: " + ex.toString());
            return ResponseUtil.fail();
        }
    }

}
