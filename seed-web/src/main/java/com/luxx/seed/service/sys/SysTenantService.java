package com.luxx.seed.service.sys;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.dao.SysTenantMapper;
import com.luxx.seed.model.system.Tenant;
import com.luxx.seed.util.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SysTenantService {
    @Autowired
    private SysTenantMapper sysTenantMapper;

    public List<Tenant> getAllTenants() {
        return sysTenantMapper.getAllTenants(Status.ENABLED.getCode());
    }

    public List<Tenant> getTenants(String order, String direction) {
        return sysTenantMapper.getTenants(Status.ENABLED.getCode(), order, direction);
    }

    public boolean createTenant(Tenant tenant) {
        tenant.setId(ObjectId.getUuid());
        tenant.setStatus(Status.ENABLED.getCode());
        Date now = new Date();
        tenant.setCreateTime(now);
        tenant.setUpdateTime(now);
        return sysTenantMapper.createTenant(tenant) > 0;
    }

    public boolean deleteTenant(String id) {
        return sysTenantMapper.updateTenantStatus(id, Status.DISABLED.getCode(), new Date()) > 0;
    }
}
