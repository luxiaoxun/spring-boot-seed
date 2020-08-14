package com.luxx.seed.service.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SocketManager {
    private static ConcurrentHashMap<String, WebSocketSession> manager = new ConcurrentHashMap<String, WebSocketSession>();

    public static void add(String key, WebSocketSession webSocketSession) {
        log.info("Add websocket connection: {}", key);
        manager.put(key, webSocketSession);
    }

    public static void remove(String key) {
        log.info("Remove websocket connection: {}", key);
        manager.remove(key);
    }

    public static WebSocketSession get(String key) {
        log.info("Get websocket connection: {}", key);
        return manager.get(key);
    }


}
