package com.luxx.engine.process;

import com.lmax.disruptor.WorkHandler;
import com.luxx.engine.model.event.LogEvent;
import com.luxx.engine.model.event.RingBufferEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataHandler implements WorkHandler<RingBufferEvent> {

    private DataProcessor dataProcessor;

    public DataHandler(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @Override
    public void onEvent(RingBufferEvent ringBufferEvent) throws Exception {
        try {
            log.debug(String.format("Message handler -> %s", ringBufferEvent.toString()));
            LogEvent logEvent = new LogEvent(ringBufferEvent.getData());
            dataProcessor.handleEvent(logEvent);
        } catch (Exception ex) {
            log.error("Message handler error: {}", ex.toString());
        }
    }
}
