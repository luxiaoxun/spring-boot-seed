package com.luxx.seed.service;

import com.luxx.seed.dao.UserMapper;
import com.luxx.seed.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findByUsername(String name) {
        return userMapper.getUserByName(name);
    }
}
