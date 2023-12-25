package com.traders.tradersback.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_reset_token_num")
    private Long passwordResetTokenNum;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // 게터와 세터
    public Long getPasswordResetTokenNum() {
        return passwordResetTokenNum;
    }

    public void setPasswordResetTokenNum(Long passwordResetTokenNum) {
        this.passwordResetTokenNum = passwordResetTokenNum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    // 기타 필요한 메소드
}
