package com.traders.tradersback.controller;

import com.traders.tradersback.dto.ChatMessageDTO;
import com.traders.tradersback.dto.ChatMessageResponseDTO;
import com.traders.tradersback.model.ChatMessage;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.List;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;
    @Autowired
    private MemberRepository memberRepository;

    // 메시지 처리 메서드 예시
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(ChatMessageDTO chatMessageDTO) {
        // 메시지 수신 로그
        logger.info("Received message: {}", chatMessageDTO);

        // 메시지 내용이 null이거나 비어있는지 확인
        if (chatMessageDTO.getMessage() == null || chatMessageDTO.getMessage().trim().isEmpty()) {
            logger.warn("Message content is null or empty");
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        // 데이터베이스에 저장하기 전 로그
        logger.info("Saving message to database: {}", chatMessageDTO);

        ChatMessage chatMessage = convertToEntity(chatMessageDTO);
        chatService.saveMessage(chatMessage);

        // 데이터베이스에 저장 후 로그
        logger.info("Message saved to database successfully");

        return chatMessageDTO;
    }

    // DTO에서 엔티티로 변환하는 메서드, 필요에 따라 구현
    private ChatMessage convertToEntity(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(chatMessageDTO.getMessage());

        // member_name을 사용하여 Member 엔티티 조회
        Member member = memberRepository.findByMemberId(chatMessageDTO.getSenderName());
        if (member != null) {
            chatMessage.setSenderId(member.getMemberNum()); // Member의 member_num을 senderId로 설정
        } else {
            throw new EntityNotFoundException("Member not found with name: " + chatMessageDTO.getSenderName());
        }

        chatMessage.setChatRoomId(chatMessageDTO.getChatRoomId());
        // 여기에 추가 필드 설정
        return chatMessage;
    }

    @GetMapping("/room/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDTO>> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        List<ChatMessageResponseDTO> messages = chatService.getMessagesByChatRoomIdWithNames(chatRoomId);
        return ResponseEntity.ok(messages);
    }
}