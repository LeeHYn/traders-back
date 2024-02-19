package com.traders.tradersback.service;

import com.traders.tradersback.model.Transaction;
import com.traders.tradersback.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    // 사용자의 거래 내역을 가져오는 메소드
    public List<Transaction> getTransactionsByUserId(Long userId) {
        List<Transaction> buyerTransactions = transactionRepository.findByBuyerNum(userId);
        List<Transaction> sellerTransactions = transactionRepository.findBySellerNum(userId);

        // 구매자와 판매자로서의 거래 내역을 모두 합쳐서 반환
        buyerTransactions.addAll(sellerTransactions);
        return buyerTransactions;
    }
}
