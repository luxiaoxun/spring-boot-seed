package com.luxx.seed.dao;

import com.luxx.seed.model.system.User;
import com.luxx.seed.model.system.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SysUserMapper {
    User getByUsername(String username);

    List<User> getUsers(String username, Integer status, String order, String direction);

    int createUser(User user);

    int insertUserRoles(Collection<UserRole> dataList);

    int updateUser(User user);

    int deleteUser(Long id);

    int deleteUserRoles(Long userId);

    int batchUpdateUsers(List<User> users);
}
