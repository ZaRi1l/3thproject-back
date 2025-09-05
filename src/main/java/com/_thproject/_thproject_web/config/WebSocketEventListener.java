package com._thproject._thproject_web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    // 메모리 상에서 실시간으로 접속자 목록을 관리합니다. (Key: userId, Value: userName)
    private final Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    // WebSocket 연결 이벤트 처리
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection.");
    }

    // WebSocket 연결 해제 이벤트 처리
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (sessionAttributes != null) {
            String userId = (String) sessionAttributes.get("userId");
            if (userId != null) {
                log.info("User Disconnected : " + userId);
                onlineUsers.remove(userId); // 접속자 목록에서 제거
                broadcastOnlineUsers(); // 변경된 목록을 모든 클라이언트에게 전파
            }
        }
    }

    // 새로운 사용자가 채팅에 참여했을 때 호출될 메소드 (ChatController에서 호출)
    public void userJoined(String userId, String username) {
        onlineUsers.put(userId, username);
        broadcastOnlineUsers();
    }

    // 현재 접속 중인 모든 사용자 목록을 반환하는 메소드 (REST API에서 사용)
    public Map<String, String> getOnlineUsers() {
        return onlineUsers;
    }

    // 변경된 접속자 목록을 '/topic/onlineUsers'를 구독하는 모든 클라이언트에게 전송
    private void broadcastOnlineUsers() {
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers);
    }
}