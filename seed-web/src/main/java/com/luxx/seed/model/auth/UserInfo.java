package com.luxx.seed.model.auth;

import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private boolean isAdmin;
    private List<Role> roles;
    private List<Menu> menus;
    private String token;
}
