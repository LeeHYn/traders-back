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
    //채팅방 생성
    public ChatRoom createOrGetChatRoom(Long transactionNum, Long sellerId, Long buyerId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByTransactionNum(transactionNum);

        return existingRoom.orElseGet(() -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setTransactionId(transactionNum);
            chatRoom.setSellerId(sellerId);
            chatRoom.setBuyerId(buyerId);
            chatRoom.setStatus("거래중"); // 초기 상태 설정
            return chatRoomRepository.save(chatRoom);
        });
    }

    //채팅방 상태 변경
    public void updateChatRoomStatus(Long chatRoomId, String status) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalStateException("ChatRoom with id " + chatRoomId + " not found"));
        chatRoom.setStatus(status);
        chatRoomRepository.save(chatRoom);
    }

}
