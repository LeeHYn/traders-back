package com.traders.tradersback.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_num")
    private Long productNum;

    @Column(name = "member_num")
    private Long memberNum;

    @Column(name = "main_category_num")
    private Long mainCategoryNum;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_condition")
    private String productCondition;

    @Column(name = "product_status")
    private String productStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //서버 측에서 자동으로 현재 시각을 설정하여 데이터베이스에 저장합니다.
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    // 게터와 세터

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    public Long getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Long memberNum) {
        this.memberNum = memberNum;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

