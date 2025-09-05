package com._thproject._thproject_web.postgresql.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class ChatMessageDto {
    private String content;
    private String senderId;
    private String senderName;
    private String recipientId;
    private OffsetDateTime createdAt;
}