package com.luxx.seed.model.system;

import lombok.Data;

@Data
public class RoleMenu {
    private Long id;
    private Long roleId;
    private Long menuId;

    public RoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
