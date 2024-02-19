package com.traders.tradersback.model;


import javax.persistence.*;

@Entity
@Table(name = "chat_room")
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_num")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "buyer_id")
    private Long buyerId;
    @Column(name = "transaction_num")
    private Long transactionNum; // 거래 ID
    @Column(name = "product_num")
    private Long productId; // 거래 ID
    @Column(name = "status")
    private String status; // 채팅방의 상태
    @Column(name = "product_name")
    private String productName; // 추가된 필드
    // 게터와 세터
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionId(Long transactionNum) {
        this.transactionNum = transactionNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
