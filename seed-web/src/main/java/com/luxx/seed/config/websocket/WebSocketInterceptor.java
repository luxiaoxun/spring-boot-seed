package com.luxx.seed.config.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.luxx.seed.constant.WebSshConstant;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String loginId = StpUtil.getLoginIdAsString();
            //将user id放到web socket session中
            attributes.put(WebSshConstant.USER_ID_KEY, loginId);
            return true;
        } else {
            return false;
        }
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
    }
}
