package com.traders.tradersback.service;

import com.traders.tradersback.dto.ChatMessageResponseDTO;
import com.traders.tradersback.model.ChatMessage;
import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.repository.ChatMessageRepository;
import com.traders.tradersback.repository.ChatRoomRepository;
import com.traders.tradersback.repository.MemberRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private MemberRepository memberRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        // 채팅방 존재 여부 확인
        if (!chatRoomRepository.existsById(chatMessage.getChatRoomId())) {
            throw new EntityNotFoundException("채팅방을 찾을 수 없습니다: " + chatMessage.getChatRoomId());
        }

        try {
            // 메시지에 현재 시각을 타임스탬프로 설정
            chatMessage.setTimestamp(LocalDateTime.now());
            // 메시지를 데이터베이스에 저장
            return chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            // 메시지 저장 중 발생하는 예외를 처리
            throw new ServiceException("메시지 저장 중 오류가 발생했습니다.", e);
        }
    }

    public List<ChatMessageResponseDTO> getMessagesByChatRoomIdWithNames(Long chatRoomId) {
        logger.debug("Fetching messages for chat room ID: {}", chatRoomId);
        // 주어진 채팅방 ID에 대한 모든 메시지를 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        if (messages.isEmpty()) {
            // 조회된 메시지가 없을 경우, 빈 리스트 반환
            logger.info("No messages found for chatRoomId: {}. Returning empty list.", chatRoomId);
            return new ArrayList<>();
        }

        // 채팅방, 구매자, 판매자 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found: " + chatRoomId));
        Member seller = memberRepository.findById(chatRoom.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found: " + chatRoom.getSellerId()));
        Member buyer = memberRepository.findById(chatRoom.getBuyerId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found: " + chatRoom.getBuyerId()));

        // 조회된 메시지를 ChatMessageResponseDTO로 변환하여 반환
        // 각 메시지에 대해 송신자와 수신자의 이름을 설정
        return messages.stream().map(message -> {
            String senderName;
            String otherPartyName;

            if (message.getSenderId().equals(seller.getMemberNum())) {
                senderName = seller.getMemberName();
                otherPartyName = buyer.getMemberName();
            } else if (message.getSenderId().equals(buyer.getMemberNum())) {
                senderName = buyer.getMemberName();
                otherPartyName = seller.getMemberName();
            } else {
                // 송신자가 구매자나 판매자와 일치하지 않는 경우의 처리
                logger.warn("Message sender does not match buyer or seller in chatRoomId: {}", chatRoomId);
                senderName = "알 수 없음";
                otherPartyName = "알 수 없음";
            }

            return new ChatMessageResponseDTO(senderName, otherPartyName, message.getMessage(), message.getChatRoomId(), message.getTimestamp());
        }).collect(Collectors.toList());
    }
}
