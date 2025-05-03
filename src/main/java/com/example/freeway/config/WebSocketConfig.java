package com.example.freeway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // <-- сервер рассылает на /topic/**
        config.setApplicationDestinationPrefixes("/app"); // <-- клиент может отправлять на /app/**
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-connection") // <--- основной endpoint
                .setAllowedOriginPatterns("*") // ВАЖНО! ИСПОЛЬЗУЙ это, а не setAllowedOrigins()
                .withSockJS(); // включаем SockJS (fallback на HTTP)
    }
}
