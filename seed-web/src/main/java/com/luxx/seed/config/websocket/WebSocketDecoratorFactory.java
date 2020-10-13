package com.luxx.seed.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;

@Component
@Slf4j
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {
    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("WebSocket connection established, sessionId = {}", session.getId());
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    log.info("WebSocket principal name = {} ", principal.getName());
                    SocketManager.add(principal.getName(), session);
                }
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("WebSocket connection closed, sessionId = {}", session.getId());
                Principal principal = session.getPrincipal();
                if (principal != null) {
                    SocketManager.remove(principal.getName());
                }
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}
