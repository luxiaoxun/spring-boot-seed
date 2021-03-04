package com.luxx.task;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppInfoTask {
    @Scheduled(cron = "0 */15 * * * *")
    @SchedulerLock(name = "app-info-task", lockAtMostFor = "15m", lockAtLeastFor = "15m")
    public void scheduledTask() {
        // do something
    }
}
