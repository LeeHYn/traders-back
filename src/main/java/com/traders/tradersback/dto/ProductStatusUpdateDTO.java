package com.traders.tradersback.dto;

public class ProductStatusUpdateDTO {
    private Long productId;
    private String newStatus;

    // 게터와 세터
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
