package com.example.freeway.service.impl;

import com.example.freeway.db.entity.EmailVerificationToken;
import com.example.freeway.db.repository.EmailVerificationTokenRepository;
import com.example.freeway.exception.BadRequestException;
import com.example.freeway.util.CustomMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final CustomMailSender customMailSender;

    public void sendVerificationCode(String email) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        EmailVerificationToken token = EmailVerificationToken.builder()
                .email(email)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(4))
                .build();
        tokenRepository.save(token);

        customMailSender.send(email, "Подтверждение Email", "Ваш код подтверждения: " + code);
    }

    public boolean verifyCode(String email, String code) {
        EmailVerificationToken token = tokenRepository.findByEmailAndCodeAndUsedFalse(email, code)
                .orElseThrow(() -> new BadRequestException("Код подтверждения не найден или уже использован"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Срок действия кода истёк");
        }

        token.setUsed(true);
        tokenRepository.save(token);
        return true;
    }
}
