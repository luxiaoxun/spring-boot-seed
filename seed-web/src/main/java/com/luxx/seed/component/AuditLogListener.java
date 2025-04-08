package com.luxx.seed.component;

import com.luxx.seed.component.event.AuditLogEvent;
import com.luxx.seed.model.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditLogListener {

    @Async
    @Order
    @EventListener(AuditLogEvent.class)
    public void handler(AuditLogEvent event) {
        AuditLog sysLog = (AuditLog) event.getSource();
        log.info("AuditLogEvent: {}", sysLog);
    }

}
