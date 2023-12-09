package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(Long sellerId, Long buyerId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setSellerId(sellerId);
        chatRoom.setBuyerId(buyerId);
        return chatRoomRepository.save(chatRoom);
    }
}
