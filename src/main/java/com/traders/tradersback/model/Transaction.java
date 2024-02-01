package com.traders.tradersback.model;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionNum;

    @Column(name = "seller_num")
    private Long sellerNum;

    @Column(name = "buyer_num")
    private Long buyerNum;

    @Column(name = "product_num")
    private Long productNum;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "transaction_status")
    private String transactionStatus;



    // 게터와 세터

    public Long getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(Long transactionNum) {
        this.transactionNum = transactionNum;
    }

    public Long getSellerNum() {
        return sellerNum;
    }

    public void setSellerNum(Long sellerNum) {
        this.sellerNum = sellerNum;
    }

    public Long getBuyerNum() {
        return buyerNum;
    }

    public void setBuyerNum(Long buyerNum) {
        this.buyerNum = buyerNum;
    }

    public Long getProductNum() {
        return productNum;
    }

    public void setProductNum(Long productNum) {
        this.productNum = productNum;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
