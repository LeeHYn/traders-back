package com.traders.tradersback.repository;

import com.traders.tradersback.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 추가적인 쿼리 메서드가 필요한 경우 여기에 정의합니다.
}
