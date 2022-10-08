package com.luxx.seed.dao;

import com.luxx.seed.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    Long getUserCount();

    User getUserByName(String name);

    int createUser(User user);

    void updateUser(User user);
}
