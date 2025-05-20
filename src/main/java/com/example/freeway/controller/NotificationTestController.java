package com.example.freeway.controller;


import com.example.freeway.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-notification")
@RequiredArgsConstructor
public class NotificationTestController {

    private final NotificationService notificationService;

    @PostMapping
    public String sendTestNotification(@RequestParam Long userId) {
        notificationService.sendNotification(userId, "Тестовое уведомление", "Это сообщение было отправлено через WebSocket!");
        return "Уведомление отправлено пользователю с ID = " + userId;
    }
}
