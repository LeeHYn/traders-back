package com.traders.tradersback.controller;

import com.traders.tradersback.dto.EmailRequestDTO;
import com.traders.tradersback.dto.PasswordResetRequestDTO;
import com.traders.tradersback.model.EmailVerification;
import com.traders.tradersback.repository.EmailVerificationRepository;
import com.traders.tradersback.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/api/email")
public class EmailVerificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody EmailRequestDTO emailRequest) {
        try {
            emailService.sendEmailWithVerificationCode(emailRequest.getEmail());
            return ResponseEntity.ok("Verification email sent");
        } catch (Exception ex) {
            // 로깅이나 추가 작업이 필요할 경우 여기에 추가
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending verification email: " + ex.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailRequestDTO verificationRequest) {
        try {
            String result = emailService.verifyEmail(verificationRequest.getEmail(), verificationRequest.getCode());
            if (result.equals("Email successfully verified.")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verifying email: " + ex.getMessage());
        }
    }



}

