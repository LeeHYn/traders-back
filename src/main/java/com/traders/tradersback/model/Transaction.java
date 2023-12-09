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
}
