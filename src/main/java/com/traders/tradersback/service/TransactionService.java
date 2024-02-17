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

    public List<Transaction> getTransactionsByBuyerId(Long buyerId) {
        return transactionRepository.findByBuyerNum(buyerId);
    }
}
