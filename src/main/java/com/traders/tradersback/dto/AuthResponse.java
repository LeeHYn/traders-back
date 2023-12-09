package com.traders.tradersback.dto;

import com.traders.tradersback.model.Member;

public class AuthResponse {
    private String member;
    private String token;

    public AuthResponse(String member, String token) {
        this.member = member;
        this.token = token;
    }

    // 게터와 세터
    public String getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = String.valueOf(member);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
