package com.traders.tradersback.repository;

import com.traders.tradersback.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT p.mainCategoryNum, COUNT(t) FROM Transaction t JOIN Product p ON t.productNum = p.productNum " +
            "GROUP BY p.mainCategoryNum ORDER BY COUNT(t) DESC")
    List<Object[]> findTopMainCategories();

    // 거래 정보 조회를 위한 메소드
    Optional<Transaction> findByProductIdAndSellerIdAndBuyerId(Long productId, Long sellerId, Long buyerId);
}
