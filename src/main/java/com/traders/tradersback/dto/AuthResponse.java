package com.traders.tradersback.dto;

import com.traders.tradersback.model.Member;

public class AuthResponse {

    private Long memberNum; // 추가
    private String member;
    private String accessToken;

    // 생성자에 memberNum 추가
    public AuthResponse(Long memberNum, String member, String accessToken) {
        this.memberNum = memberNum;
        this.member = member;
        this.accessToken = accessToken;
    }

    public AuthResponse(String member, String accessToken) {
        this.member = member;
        this.accessToken = accessToken;
    }

    public Long getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Long memberNum) {
        this.memberNum = memberNum;
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
