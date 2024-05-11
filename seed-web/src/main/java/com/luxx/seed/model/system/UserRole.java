package com.luxx.seed.model.system;

import lombok.Data;

@Data
public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
