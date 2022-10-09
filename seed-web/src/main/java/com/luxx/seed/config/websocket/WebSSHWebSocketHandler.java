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
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("Connection established, user key:{}", webSocketSession.getAttributes().get(WebSshConstant.USER_UUID_KEY));
        //调用初始化连接
        webSSHService.initConnection(webSocketSession);
    }

    //收到消息的回调
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
            log.info("user:{}, command:{}", webSocketSession.getAttributes().get(WebSshConstant.USER_UUID_KEY),
                    webSocketMessage.toString());
            //调用service接收消息
            webSSHService.handleMessage(((TextMessage) webSocketMessage).getPayload(), webSocketSession);
        } else if (webSocketMessage instanceof BinaryMessage) {
            log.warn("Can not handle binary message");
        } else {
            log.warn("Unexpected WebSocket message type: " + webSocketMessage);
        }
    }

    //出现错误的回调
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        log.error("Transport error");
    }

    //连接关闭的回调
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed, user key:{}", webSocketSession.getAttributes().get(WebSshConstant.USER_UUID_KEY));
        //调用service关闭连接
        webSSHService.close(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
