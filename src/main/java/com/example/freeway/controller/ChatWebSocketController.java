package com.example.freeway.controller;

import com.example.freeway.db.entity.ChatMessage;
import com.example.freeway.db.entity.SysUser;
import com.example.freeway.model.chat.ChatMessageDto;
import com.example.freeway.service.SysUserService;
import com.example.freeway.service.impl.ChatService;
import com.example.freeway.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final SysUserService userService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto dto) {
        SysUser sender = userService.findById(dto.getSenderId());

        notificationService.sendNotification(
                dto.getRecipientId(),
                "Новое сообщение",
                "У вас новое сообщение от " + sender.getSecondName() + " " + sender.getName()
        );

        ChatMessage saved = chatService.save(dto);

        messagingTemplate.convertAndSendToUser(
                dto.getRecipientId().toString(),
                "/queue/messages",
                dto
        );
    }
}
