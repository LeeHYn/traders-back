package com.traders.tradersback.repository;

import com.traders.tradersback.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySellerIdAndBuyerId(Long sellerId, Long buyerId);
    Optional<ChatRoom> findByTransactionNum(Long transactionNum);

    Optional<ChatRoom> findByProductIdAndSellerIdAndBuyerId(Long productId, Long sellerId, Long buyerId);
}
