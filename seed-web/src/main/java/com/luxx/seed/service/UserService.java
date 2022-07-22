package com.luxx.seed.service;

import com.luxx.seed.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    public User findUserById(String userId) {
        return new User(1, "luxiaoxun", "123456");
    }

    public User findByUsername(String username) {
        return new User(1, "luxiaoxun", "123456");
    }
}
