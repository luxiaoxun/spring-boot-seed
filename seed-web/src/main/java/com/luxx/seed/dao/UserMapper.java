package com.luxx.seed.dao;

import com.luxx.seed.model.system.User;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getUsers(String username, Integer status, String order, String direction);

    int createUser(User user);

    int updateUser(User user);

    int updateUserStatus(Long id, Integer status, Date updateTime);

    int updateUserPassword(Long id, String password, Date updateTime);

    int deleteUser(Long id);
}
