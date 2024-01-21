package com.traders.tradersback.dto;

import com.traders.tradersback.model.Member;

public class AuthResponse {
    private String member;
    private String accessToken;


    public AuthResponse(String member, String accessToken) {
        this.member = member;
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

}
