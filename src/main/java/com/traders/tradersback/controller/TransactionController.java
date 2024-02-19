package com.traders.tradersback.controller;
import com.traders.tradersback.model.Transaction;
import com.traders.tradersback.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    // 사용자의 거래 내역을 가져오는 API 엔드포인트
    @GetMapping("/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable Long userId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // 로그 및 오류 처리
            return ResponseEntity.internalServerError().build();
        }
    }
}
