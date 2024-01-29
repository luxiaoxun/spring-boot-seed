package com.luxx.engine.data;

import com.luxx.engine.model.event.LogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataProcessor {

    public void handleEvent(LogEvent event) {
        log.info("Receive event: {}", event);
    }

}
