package com.example.freeway.controller;

import com.example.freeway.db.entity.ChatMessage;
import com.example.freeway.db.repository.ChatMessageRepository;
import com.example.freeway.model.chat.ChatMessageDto;
import com.example.freeway.service.impl.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;
    private final ChatMessageRepository repository;

    @GetMapping("/conversation")
    public List<ChatMessageDto> getConversation(
            @RequestParam Long user1,
            @RequestParam Long user2) {
        return chatService.getConversation(user1, user2);
    }

    @GetMapping("/partners")
    public Set<Long> getChatPartners(@RequestParam Long userId) {
        List<ChatMessage> messages = repository.findAllBySenderIdOrRecipientId(userId, userId);
        return messages.stream()
                .flatMap(m -> Stream.of(m.getSenderId(), m.getRecipientId()))
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toSet());
    }
}
