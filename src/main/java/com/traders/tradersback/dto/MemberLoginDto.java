package com.traders.tradersback.dto;

public class MemberLoginDto {
    private String memberId;
    private String memberPassword;

    // 게터와 세터
    // memberId에 대한 게터
    public String getMemberId() {
        return memberId;
    }

    // memberId에 대한 세터
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    // password에 대한 게터
    public String getMemberPassword() {
        return memberPassword;
    }

    // password에 대한 세터
    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }
}
