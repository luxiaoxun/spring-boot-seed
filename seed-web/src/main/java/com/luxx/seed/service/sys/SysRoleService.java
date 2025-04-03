package com.luxx.seed.service.sys;

import com.luxx.seed.dao.RoleMapper;
import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.MenuTree;
import com.luxx.seed.model.system.Role;
import com.luxx.seed.model.system.RoleMenu;
import com.luxx.seed.response.Response;
import com.luxx.seed.response.ResponseUtil;
import com.luxx.seed.util.UserUtil;
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
    private RoleMapper roleMapper;

    public List<Role> getRoles() {
        return roleMapper.getAllRoles();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response createRole(Role role) {
        role.setBuiltin(0);
        role.setCreateUser(UserUtil.getLoginUsername());
        role.setUpdateUser(UserUtil.getLoginUsername());
        Date now = new Date();
        role.setCreateTime(now);
        role.setUpdateTime(now);
        roleMapper.insertRole(role);
        Long roleId = role.getId();
        List<Long> menuIds = role.getMenuIds();
        List<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            roleMenus.add(new RoleMenu(roleId, menuId));
        }
        roleMapper.insertRoleMenus(roleMenus);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response updateRole(Role role) {
        role.setUpdateUser(UserUtil.getLoginUsername());
        role.setUpdateTime(new Date());
        roleMapper.updateRole(role);
        Long roleId = role.getId();
        List<Long> menuIds = role.getMenuIds();
        List<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            roleMenus.add(new RoleMenu(roleId, menuId));
        }
        roleMapper.deleteRoleMenus(roleId);
        roleMapper.insertRoleMenus(roleMenus);
        return ResponseUtil.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public Response deleteRole(Long roleId) {
        roleMapper.deleteRoleMenus(roleId);
        roleMapper.deleteRoleUsers(roleId);
        roleMapper.deleteRole(roleId);
        return ResponseUtil.success();
    }

    public List<MenuTree> getMenuTreeList(Long id) {
        List<Menu> menuList = roleMapper.getAllMenus();

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
