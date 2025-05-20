package com.example.freeway.model.chat;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime timestamp;
}
