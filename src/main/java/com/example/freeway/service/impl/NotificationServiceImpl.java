package com.example.freeway.service.impl;

import com.example.freeway.model.notification.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {
    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(Long userId, String title, String message) {
        NotificationDto dto = new NotificationDto(title, message);
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, dto);
    }
}
