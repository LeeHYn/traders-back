package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatMessage;
import com.traders.tradersback.repository.ChatMessageRepository;
import com.traders.tradersback.repository.ChatRoomRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        // 채팅방 존재 여부 확인
        if (!chatRoomRepository.existsById(chatMessage.getChatRoomId())) {
            throw new EntityNotFoundException("채팅방을 찾을 수 없습니다: " + chatMessage.getChatRoomId());
        }

        try {
            chatMessage.setTimestamp(LocalDateTime.now());
            return chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            throw new ServiceException("메시지 저장 중 오류가 발생했습니다.", e);
        }
    }

    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }
}
