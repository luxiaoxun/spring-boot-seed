package com.luxx.seed.service;

import com.luxx.seed.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User findUserById(String userId) {
        return new User("1", "luxiaoxun", "123456");
    }

    public User findByUsername(String username) {
        return new User("1", "luxiaoxun", "123456");
    }
}
