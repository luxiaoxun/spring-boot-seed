package com.luxx.engine.data;

import com.lmax.disruptor.ExceptionHandler;
import org.slf4j.Logger;

public class LoggingExceptionHandler implements ExceptionHandler<Object> {
    private final Logger logger;

    public LoggingExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handleEventException(Throwable ex, long sequence, Object event) {
        String eventStr;
        try {
            eventStr = event.toString();
        } catch (Exception e) {
            eventStr = "<invalid event data>";
        }
        logger.warn("Unable to process event " + eventStr + ", sequence " + sequence, ex);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("Exception occurred while starting disruptor.", ex);
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("Exception occurred while stopping disruptor.", ex);
    }
}
