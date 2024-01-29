package com.luxx.engine.bootstrap;

import com.luxx.engine.process.EventConsumer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Server implements InitializingBean, DisposableBean {
    @Autowired
    private EventConsumer eventConsumer;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void destroy() throws Exception {
        eventConsumer.stop();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppStarted() {
        eventConsumer.start();
    }
}
