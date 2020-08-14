package com.luxx.seed.controller.websocket;

import com.luxx.seed.controller.BaseController;
import com.luxx.seed.service.websocket.SocketManager;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@RestController
@RequestMapping("websocket")
@Api(tags = "websocket")
@Slf4j
public class WebsocketController extends BaseController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 点对点：通过 Http request /websocket/sendUser 向token发送消息
     * 前端开通 var socket = new SockJS(host+'/myUrl' + '?token=1234');
     */
    @RequestMapping("/sendUser")
    public void sendUser(String token) {
        log.info("Send 'Hello' to {}", token);
        WebSocketSession webSocketSession = SocketManager.get(token);
        if (webSocketSession != null) {
            simpMessagingTemplate.convertAndSendToUser(token, "/queue/sendUser", "Hello");
        }
    }

    /**
     * 广播：通过 Http request /websocket/sendTopic 向/topic/sendTopic发送消息
     */
    @RequestMapping("/sendTopic")
    public void sendTopic() {
        simpMessagingTemplate.convertAndSend("/topic/sendTopic", "Hello everyone");
    }

    /**
     * 广播：client通过 /websocket/sendAllUser 向/topic/sendTopic发送消息
     */
    @MessageMapping("/websocket/sendAllUser")
    @SendTo("/topic/sendTopic")
    public String sendAllUser(String message) {
        return message;
    }

    /**
     * 客户端发消息，服务端接收
     */
    @MessageMapping("/websocket/sendServer")
    public void sendServer(String message) {
        log.info("message: {}", message);
    }


    /**
     * 点对点用户聊天，这边需要注意，由于前端传过来json数据，所以使用@RequestBody
     * 这边需要前端开通var socket = new SockJS(host+'/myUrl' + '?token=4567');   token为指定name
     */
    @MessageMapping("/websocket/sendMyUser")
    public void sendMyUser(@RequestBody Map<String, String> map) {
        log.info("map = {}", map);
        WebSocketSession webSocketSession = SocketManager.get(map.get("name"));
        if (webSocketSession != null) {
            log.info("sessionId = {}", webSocketSession.getId());
            simpMessagingTemplate.convertAndSendToUser(map.get("name"), "/queue/sendUser", map.get("message"));
        }
    }

}
