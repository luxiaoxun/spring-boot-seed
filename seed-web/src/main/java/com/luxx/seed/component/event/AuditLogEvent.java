package com.luxx.seed.component.event;

import org.springframework.context.ApplicationEvent;

public class AuditLogEvent extends ApplicationEvent {

    public AuditLogEvent(Object source) {
        super(source);
    }
}
