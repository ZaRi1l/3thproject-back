package com._thproject._thproject_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 이 메소드의 시그니처가 정확한지 다시 확인하세요.
    // public void configureMessageBroker(MessageBrokerRegistry registry) { ... }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지를 구독할 주소의 접두사입니다.
        // /topic: 다수가 구독하는 공개 채널용
        // /user: 특정 사용자에게 1:1 메시지를 보낼 때 사용
        registry.enableSimpleBroker("/topic", "/user");

        // 클라이언트가 서버로 메시지를 보낼 때 사용할 주소의 접두사입니다.
        registry.setApplicationDestinationPrefixes("/app");

        // 1:1 메시징의 핵심 설정입니다.
        registry.setUserDestinationPrefix("/user");
    }

    // 이 메소드의 시그니처도 정확한지 다시 확인하세요.
    // public void registerStompEndpoints(StompEndpointRegistry registry) { ... }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 최초 WebSocket 연결을 시도할 엔드포인트입니다.
        // CORS 문제를 방지하기 위해 setAllowedOriginPatterns("*")를 추가합니다.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}