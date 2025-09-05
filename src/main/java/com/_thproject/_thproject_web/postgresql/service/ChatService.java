package com._thproject._thproject_web.postgresql.service;

import com._thproject._thproject_web.postgresql.dto.ChatMessageDto;
import com._thproject._thproject_web.postgresql.entity.ChatMessage;
import com._thproject._thproject_web.postgresql.entity.User;
import com._thproject._thproject_web.postgresql.repository.ChatMessageRepository;
import com._thproject._thproject_web.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage saveMessage(ChatMessageDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(dto.getContent())
                .build();

        return chatMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getConversation(String userId1, String userId2) {
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatMessage> messages = chatMessageRepository
                .findBySenderAndRecipientOrRecipientAndSenderOrderByCreatedAtAsc(user1, user2, user1, user2);

        return messages.stream()
                .map(message -> ChatMessageDto.builder()
                        .content(message.getContent())
                        .senderId(message.getSender().getUserid())
                        .senderName(message.getSender().getRealUsername())
                        .recipientId(message.getRecipient().getUserid())
                        .createdAt(message.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}