package com.traders.tradersback.repository;

import com.traders.tradersback.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 필요한 쿼리 메소드 추가
}
