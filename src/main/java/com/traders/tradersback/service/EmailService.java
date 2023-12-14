package com.traders.tradersback.service;

import com.traders.tradersback.model.EmailVerification;
import com.traders.tradersback.model.PasswordResetToken;
import com.traders.tradersback.repository.EmailVerificationRepository;
import com.traders.tradersback.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    // 이메일 인증 코드 전송
    public void sendEmailWithVerificationCode(String to) {
        String verificationCode = generateRandomSixDigitNumber();
        EmailVerification verification = new EmailVerification();
        verification.setEmail(to);
        verification.setVerificationCode(verificationCode);
        verification.setCreatedAt(LocalDateTime.now());
        verification.setExpiresAt(LocalDateTime.now().plusHours(24)); // 24시간 후 만료
        verification.setVerified(false);
        emailVerificationRepository.save(verification);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jemie9812@naver.com");
        message.setTo(to);
        message.setSubject("인증코드 발송");
        message.setText("인증코드 : ");
        message.setText(verificationCode);
        mailSender.send(message);
    }

    // 랜덤 6자리 숫자 생성
    private String generateRandomSixDigitNumber() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(number);
    }

    // 이메일 인증 확인
    public String verifyEmail(String email, String code) {
        Optional<EmailVerification> verificationOpt = emailVerificationRepository.findByEmailAndVerificationCode(email, code);
        if (verificationOpt.isPresent()) {
            EmailVerification verification = verificationOpt.get();
            if (verification.getExpiresAt().isAfter(LocalDateTime.now()) && !verification.getVerified()) {
                verification.setVerified(true);
                emailVerificationRepository.save(verification);
                return "Email successfully verified.";
            } else {
                return "Verification code is expired or already used.";
            }
        } else {
            return "Invalid verification code.";
        }
    }

    // 비밀번호 재설정
    public void sendPasswordResetEmail(String to) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(to);
        resetToken.setToken(token);
        // expiryDate를 현재 시간으로부터 5분 후로 설정
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        passwordResetTokenRepository.save(resetToken);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("jemie9812@naver.com");
        mail.setTo(to);
        mail.setSubject("비밀번호 재설정 요청");
        String resetLink = "http://localhost:3000/reset-password/" + token; // URL 수정
        mail.setText("비밀번호를 재설정하려면 다음 링크를 클릭하세요: " + resetLink);
        mailSender.send(mail);

    }
}