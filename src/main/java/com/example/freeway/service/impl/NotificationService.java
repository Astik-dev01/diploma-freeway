package com.example.freeway.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(Long userId, String title, String message) {
        var payload = Map.of("title", title, "message", message);
        System.out.println("Отправка уведомления пользователю #" + userId + " → " + title + ": " + message);

        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",
                payload
        );
    }
}
