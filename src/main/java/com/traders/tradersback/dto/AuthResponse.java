package com.traders.tradersback.dto;

import com.traders.tradersback.model.Member;

public class AuthResponse {
    private String member;
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String member, String accessToken, String refreshToken) {
        this.member = member;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    // 새로운 생성자 - 단일 매개변수
    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
