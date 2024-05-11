package com.luxx.seed.dao;

import com.luxx.seed.model.system.Menu;
import com.luxx.seed.model.system.RoleMenu;
import com.luxx.seed.model.system.User;
import com.luxx.seed.model.system.UserRole;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    User getByUsername(String username);

    List<User> getUsers(String username, Integer status, String order, String direction);

    int createUser(User user);

    int insertUserRoles(Collection<UserRole> dataList);

    int updateUser(User user);

    int deleteUser(Long id);

    int deleteUserRoles(Long userId);

    int batchUpdateUsers(List<User> users);
}
