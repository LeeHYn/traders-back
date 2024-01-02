package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createOrGetChatRoom(Long sellerId, Long buyerId) {
        // 채팅방이 이미 존재하는지 검사
        Optional<ChatRoom> existingRoom = chatRoomRepository.findBySellerIdAndBuyerId(sellerId, buyerId);

        // 존재하면 반환, 아니면 새로 생성
        return existingRoom.orElseGet(() -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setSellerId(sellerId);
            chatRoom.setBuyerId(buyerId);
            // 추가 정보 설정 (예: 상태)
            return chatRoomRepository.save(chatRoom);
        });
    }
}
