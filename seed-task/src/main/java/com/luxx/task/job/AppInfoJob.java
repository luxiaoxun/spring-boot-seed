package com.luxx.task.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class AppInfoJob {
    @XxlJob("appInfoJob")
    public void executeJob() throws Exception {
    }
}
