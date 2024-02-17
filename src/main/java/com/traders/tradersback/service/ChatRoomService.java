package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.model.Transaction;
import com.traders.tradersback.repository.ChatRoomRepository;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    public ChatRoom createOrGetChatRoom(Long sellerId, Long buyerId, Long productId, String status) {
        // 거래(transaction) 정보 생성 또는 확인
        Transaction transaction = createOrGetTransaction(sellerId, buyerId, productId);

        // 채팅방 조회 또는 생성
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByProductIdAndSellerIdAndBuyerId(productId, sellerId, buyerId);
        return existingRoom.orElseGet(() -> {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setSellerId(sellerId);
            chatRoom.setBuyerId(buyerId);
            chatRoom.setProductId(productId);
            chatRoom.setStatus(status); // 상태 설정
            chatRoom.setTransactionId(transaction.getTransactionNum()); // 거래 ID 설정
            return chatRoomRepository.save(chatRoom);
        });
    }

    private Transaction createOrGetTransaction(Long sellerId, Long buyerId, Long productId) {
        // 거래 확인 로직 (예: productId, sellerId, buyerId를 기준으로 기존 거래 확인)
        Optional<Transaction> existingTransaction = transactionRepository.findByProductNumAndSellerNumAndBuyerNum(productId, sellerId, buyerId);
        return existingTransaction.orElseGet(() -> {
            Transaction newTransaction = new Transaction();
            newTransaction.setSellerNum(sellerId);
            newTransaction.setBuyerNum(buyerId);
            newTransaction.setProductNum(productId);
            newTransaction.setTransactionDate(LocalDateTime.now()); // 현재 날짜와 시간 설정
            newTransaction.setTransactionStatus("진행중"); // 초기 상태 설정
            return transactionRepository.save(newTransaction);
        });
    }

    public void updateChatRoomStatus(Long chatRoomId, String status) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalStateException("ChatRoom with id " + chatRoomId + " not found"));
        chatRoom.setStatus(status);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 정보 조회 메소드
    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new EntityNotFoundException("ChatRoom with id " + chatRoomId + " not found"));
    }

    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        // 판매자 또는 구매자로서의 채팅방 목록을 조회
        List<ChatRoom> sellerRooms = chatRoomRepository.findBySellerId(userId);
        List<ChatRoom> buyerRooms = chatRoomRepository.findByBuyerId(userId);

        // 두 목록을 하나로 합치기
        List<ChatRoom> allRooms = new ArrayList<>();
        allRooms.addAll(sellerRooms);
        allRooms.addAll(buyerRooms);

        return allRooms;
    }
}
