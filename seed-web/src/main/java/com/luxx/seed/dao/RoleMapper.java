package com.luxx.seed.dao;

import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.model.system.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> getRolesByUserId(Long userId);

    List<Menu> getMenusByRoleId(List<Long> roleIds);

    List<Menu> getAllMenus();

    List<Role> getAllRoles();

    List<Long> getRoleMenus(Long roleId);

    int insertRole(Role role);

    int insertRoleMenus(Collection<RoleMenu> dataList);

    int updateRole(Role role);

    int deleteRole(Long id);

    int deleteRoleUsers(Long roleId);

    int deleteRoleMenus(Long roleId);

}
