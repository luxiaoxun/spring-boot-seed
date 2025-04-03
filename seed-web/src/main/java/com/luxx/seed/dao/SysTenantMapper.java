package com.luxx.seed.dao;

import com.luxx.seed.model.system.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysTenantMapper {
    List<Tenant> getAllTenants(Integer status);

    List<Tenant> getTenants(Integer status, String order, String direction);

    int createTenant(Tenant tenant);

    int updateTenantStatus(String id, Integer status, Date updateTime);

    int deleteTenant(String id);
}
