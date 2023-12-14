package com.traders.tradersback.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {
    private String memberId;
    private Long mainCategoryNum;
    private String productName;
    private Integer price;
    private String productDescription;
    private String productCondition;
    private String productStatus;

    private LocalDateTime createdAt;

    private List<String> imageUrls; // 이미지 URL 리스트

// ... 게터와 세터 ...

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Long getMainCategoryNum() {
        return mainCategoryNum;
    }

    public void setMainCategoryNum(Long mainCategoryNum) {
        this.mainCategoryNum = mainCategoryNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
