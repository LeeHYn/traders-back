package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.repository.ChatRoomRepository;
import com.traders.tradersback.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    //채팅방 생성
    public ChatRoom createOrGetChatRoom(Long transactionNum, Long sellerId, Long buyerId) {
        logger.info("Creating or getting chat room for transactionNum: {}, sellerId: {}, buyerId: {}", transactionNum, sellerId, buyerId);

        // 나머지 로직은 동일
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByTransactionNum(transactionNum);
        return existingRoom.orElseGet(() -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setTransactionId(transactionNum);
            chatRoom.setSellerId(sellerId); // sellerId는 이미 memberNum 형태로 제공됨
            chatRoom.setBuyerId(buyerId); // buyerId는 변환된 memberNum
            chatRoom.setStatus("거래중");
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
