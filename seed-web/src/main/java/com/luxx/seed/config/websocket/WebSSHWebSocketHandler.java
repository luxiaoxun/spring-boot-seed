package com.luxx.seed.config.websocket;

import com.luxx.seed.constant.WebSshConstant;
import com.luxx.seed.service.WebSSHService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
@Slf4j
public class WebSSHWebSocketHandler implements WebSocketHandler {
    @Autowired
    private WebSSHService webSSHService;

    //用户连接上WebSocket的回调
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established, user id:{}", session.getAttributes().get(WebSshConstant.USER_ID_KEY));
        //调用初始化连接
        webSSHService.initConnection(session);
    }

    //收到消息的回调
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            log.info("user:{}, command:{}", session.getAttributes().get(WebSshConstant.USER_ID_KEY),
                    message.toString());
            //调用service接收消息
            webSSHService.handleMessage(((TextMessage) message).getPayload(), session);
        } else if (message instanceof BinaryMessage) {
            log.warn("Can not handle binary message");
        } else {
            log.warn("Unexpected WebSocket message type: " + message);
        }
    }

    //出现错误的回调
    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        log.error("Transport error");
    }

    //连接关闭的回调
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed, user id:{}", session.getAttributes().get(WebSshConstant.USER_ID_KEY));
        //调用service关闭连接
        webSSHService.close(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
