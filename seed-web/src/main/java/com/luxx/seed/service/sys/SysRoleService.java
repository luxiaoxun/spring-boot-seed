package com.luxx.seed.service.sys;

import com.luxx.seed.dao.SysRoleMapper;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.MenuTree;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.model.system.RoleMenu;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    public List<Role> getRoles() {
        return sysRoleMapper.getAllRoles();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createRole(Role role) {
        role.setBuiltin(0);
        role.setCreateUser(AuthUtil.getLoginUsername());
        role.setUpdateUser(AuthUtil.getLoginUsername());
        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        sysRoleMapper.insertRole(role);
        Long roleId = role.getId();
        List<Long> menuIds = role.getMenuIds();
        List<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            roleMenus.add(new RoleMenu(roleId, menuId));
        }
        sysRoleMapper.insertRoleMenus(roleMenus);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateRole(Role role) {
        role.setUpdateUser(AuthUtil.getLoginUsername());
        role.setUpdateTime(new Date());
        sysRoleMapper.updateRole(role);
        Long roleId = role.getId();
        List<Long> menuIds = role.getMenuIds();
        List<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            roleMenus.add(new RoleMenu(roleId, menuId));
        }
        sysRoleMapper.deleteRoleMenus(roleId);
        sysRoleMapper.insertRoleMenus(roleMenus);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteRole(Long roleId) {
        sysRoleMapper.deleteRoleMenus(roleId);
        sysRoleMapper.deleteRoleUsers(roleId);
        sysRoleMapper.deleteRole(roleId);
        return ResponseUtil.success();
    }

    public List<MenuTree> getMenuTreeList(Long id) {
        List<Menu> menuList = sysRoleMapper.getAllMenus();

        Map<Long, List<MenuTree>> map = new HashMap<>();
        for (Menu menu : menuList) {
            // 构造节点信息
            MenuTree node = new MenuTree(menu);
            map.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>());
            // 每个父节点下的所有子节点
            map.get(menu.getParentId()).add(node);
        }

        //构造层级
        map.keySet().forEach(parentId -> {
            // 遍历父节点下的子节点
            for (MenuTree treeNode : map.get(parentId)) {
                // 此节点可能含有子节点，填充children
                if (!CollectionUtils.isEmpty(map.get(treeNode.getId()))) {
                    treeNode.getChildren().addAll(map.get(treeNode.getId()));
                }
            }
        });

        //设置默认取全部下的所有节点
        return map.get(id);
    }
}
