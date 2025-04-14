package com.example.freeway.scheduler;


import com.example.freeway.db.repository.PasswordResetTokenRepository;
import com.example.freeway.service.SysLogRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class Scheduler {
    private static final String CRON_EVERY_DAY_7AM = "0 0 7 * * *";
    private static final String CRON_EVERY_SUNDAY_1AM = "0 0 1 * * SUN";
    private static final String CRON_EVERY_DAY_12AM = "0 0 0 * * ?";
    private static final String CRON_EVERY_DAY_1AM = "0 0 1 * * *";

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final SysLogRequestService sysLogRequestService;

    @Autowired
    public Scheduler(

            PasswordResetTokenRepository passwordResetTokenRepository,
            SysLogRequestService sysLogRequestService) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.sysLogRequestService = sysLogRequestService;
    }

    @Scheduled(cron = CRON_EVERY_DAY_12AM) // Запуск каждый день в 00:00
    public void clearOldResetTokens() {
        Date now = new Date();
        passwordResetTokenRepository.deleteByExpiryDateBefore(now);
    }


}

