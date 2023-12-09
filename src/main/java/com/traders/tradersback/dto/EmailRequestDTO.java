package com.traders.tradersback.dto;

public class EmailRequestDTO {
    private String email;
    private String code;

    // 게터와 세터
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
