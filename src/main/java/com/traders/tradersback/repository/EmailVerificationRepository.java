package com.traders.tradersback.repository;

import com.traders.tradersback.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    // 필요한 메소드를 여기에 정의합니다.
    Optional<EmailVerification> findByEmailAndVerificationCode(String email, String verificationCode);
}
