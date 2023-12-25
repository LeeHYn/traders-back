package com.traders.tradersback.dto;

public class PasswordResetDto {
    private String token; // 비밀번호 재설정 토큰
    private String newPassword; // 새로운 비밀번호

    // 게터와 세터
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
