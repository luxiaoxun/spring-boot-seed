package com.luxx.seed.service;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.dao.TenantMapper;
import com.luxx.seed.model.system.Tenant;
import com.luxx.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TenantService {
    @Autowired
    private TenantMapper tenantMapper;

    public List<Tenant> getAllTenants() {
        return tenantMapper.getAllTenants(Status.ENABLED.getCode());
    }

    public List<Tenant> getTenants(String order, String direction) {
        return tenantMapper.getTenants(Status.ENABLED.getCode(), order, direction);
    }

    public boolean createTenant(Tenant tenant) {
        tenant.setId(CommonUtil.getUuid());
        tenant.setStatus(Status.ENABLED.getCode());
        Date now = new Date();
        tenant.setCreateTime(now);
        tenant.setUpdateTime(now);
        return tenantMapper.createTenant(tenant) > 0;
    }

    public boolean deleteTenant(String id) {
        return tenantMapper.updateTenantStatus(id, Status.DISABLED.getCode(), new Date()) > 0;
    }
}
