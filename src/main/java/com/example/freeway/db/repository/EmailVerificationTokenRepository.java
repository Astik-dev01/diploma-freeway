package com.example.freeway.db.repository;

import com.example.freeway.db.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByEmailAndCodeAndUsedFalse(String email, String code);


    void deleteAllByExpiresAtBeforeAndUsedFalse(LocalDateTime now);
}
