package com.example.freeway.service.impl;

import com.example.freeway.db.entity.ChatMessage;
import com.example.freeway.db.repository.ChatMessageRepository;
import com.example.freeway.model.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessageDto dto) {
        ChatMessage message = ChatMessage.builder()
                .senderId(dto.getSenderId())
                .recipientId(dto.getRecipientId())
                .content(dto.getContent())
                .timestamp(LocalDateTime.now())
                .status(ChatMessage.MessageStatus.SENT)
                .build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessageDto> getConversation(Long user1, Long user2) {
        return chatMessageRepository
                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(user1, user2, user1, user2)
                .stream()
                .map(msg -> ChatMessageDto.builder()
                        .senderId(msg.getSenderId())
                        .recipientId(msg.getRecipientId())
                        .content(msg.getContent())
                        .timestamp(msg.getTimestamp())
                        .build())
                .toList();
    }
}
