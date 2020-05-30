package com.luxx.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

@Component
@JobHandler("appInfoCollectTask")
public class AppInfoCollectTask extends IJobHandler {
    @Override
    public ReturnT<String> execute(String param) {
        return ReturnT.SUCCESS;
    }

}
