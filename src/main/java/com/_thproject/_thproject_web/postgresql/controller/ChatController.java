package com._thproject._thproject_web.postgresql.controller;

import com._thproject._thproject_web.postgresql.dto.ChatMessageDto;
import com._thproject._thproject_web.postgresql.dto.UserResponseDto;
import com._thproject._thproject_web.postgresql.entity.ChatMessage;
import com._thproject._thproject_web.postgresql.service.ChatService;
import com._thproject._thproject_web.postgresql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com._thproject._thproject_web.config.WebSocketEventListener;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketEventListener webSocketEventListener;

    // WebSocket을 통해 1:1 메시지를 처리하는 메소드
    @MessageMapping("/chat.privateMessage")
    public void processPrivateMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessage savedMessage = chatService.saveMessage(chatMessageDto);
        chatMessageDto.setCreatedAt(savedMessage.getCreatedAt());

        // 메시지를 받는 사람의 개인 큐(/user/{recipientId}/queue/private)로 메시지를 보냅니다.
        messagingTemplate.convertAndSendToUser(
                chatMessageDto.getRecipientId(),
                "/queue/private",
                chatMessageDto
        );
    }

    // REST API를 통해 두 사용자 간의 대화 내역을 조회하는 메소드
    @GetMapping("/api/chat/history/{userId1}/{userId2}")
    @ResponseBody
    public ResponseEntity<List<ChatMessageDto>> getConversationHistory(
            @PathVariable String userId1,
            @PathVariable String userId2) {
        return ResponseEntity.ok(chatService.getConversation(userId1, userId2));
    }

    // REST API를 통해 모든 사용자 목록을 조회하는 메소드
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @MessageMapping("/chat.join")
    public void joinChat(@Payload ChatMessageDto chatMessageDto, SimpMessageHeaderAccessor headerAccessor) {
        // WebSocket 세션에 사용자 ID를 저장합니다 (연결 해제 시 사용하기 위함).
        headerAccessor.getSessionAttributes().put("userId", chatMessageDto.getSenderId());

        // EventListener를 통해 새로운 사용자 접속 처리
        webSocketEventListener.userJoined(chatMessageDto.getSenderId(), chatMessageDto.getSenderName());
    }

    // REST API: 현재 접속 중인 사용자 목록을 반환
    @GetMapping("/api/chat/online-users")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getOnlineUsers() {
        return ResponseEntity.ok(webSocketEventListener.getOnlineUsers());
    }

}