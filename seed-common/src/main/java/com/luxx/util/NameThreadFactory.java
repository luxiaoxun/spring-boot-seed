package com.luxx.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameThreadFactory implements ThreadFactory {

    private final AtomicInteger index = new AtomicInteger();
    private final String name;
    
    private final boolean daemon;
    
    public NameThreadFactory(String name) {
        this(name, false);
    }
    
    public NameThreadFactory(String name, boolean daemon) {
        super();
        this.name = name;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, name + "-" + index.getAndIncrement());
        thread.setDaemon(daemon);
        return thread;
    }
}
