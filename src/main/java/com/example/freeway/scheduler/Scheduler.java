package com.example.freeway.scheduler;


import com.example.freeway.db.repository.EmailVerificationTokenRepository;
import com.example.freeway.db.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class Scheduler {
    private static final String CRON_EVERY_DAY_12AM = "0 0 0 * * ?";

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;


    @Scheduled(cron = CRON_EVERY_DAY_12AM) // Запуск каждый день в 00:00
    public void clearOldResetTokens() {
        Date now = new Date();
        passwordResetTokenRepository.deleteByExpiryDateBefore(now);
    }
    @Scheduled(fixedRate = 3600000) // каждый час
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        emailVerificationTokenRepository.deleteAllByExpiresAtBeforeAndUsedFalse(now);
    }

}

