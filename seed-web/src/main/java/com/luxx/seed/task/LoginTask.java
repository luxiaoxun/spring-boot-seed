package com.luxx.seed.task;

import com.luxx.seed.constant.enums.Status;
import com.luxx.seed.model.system.User;
import com.luxx.seed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoginTask {
    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 120000) // Check every 2 minute
    public void unlockAccounts() {
        List<User> lockedUsers = userService.findLockedUsers();
        if (lockedUsers != null && !lockedUsers.isEmpty()) {
            DateTime currentTime = new DateTime();
            List<User> unlockedUsers = new ArrayList<>();
            for (User user : lockedUsers) {
                DateTime lockedTime = new DateTime(user.getLockedTime());
                if (lockedTime.plusMinutes(5).isBefore(currentTime)) {
                    user.setStatus(Status.ENABLED.getCode());
                    user.setLoginAttempts(0);
                    user.setLockedTime(null);
                    unlockedUsers.add(user);
                }
            }
            if (!unlockedUsers.isEmpty()) {
                userService.updateUserLockedStatus(unlockedUsers);
                log.info("Unlock {} user accounts", unlockedUsers.size());
            }
        }
    }
}
