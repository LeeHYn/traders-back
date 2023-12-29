package com.traders.tradersback.service;

import com.traders.tradersback.model.ChatMessage;
import com.traders.tradersback.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    // 추가적인 메소드, 예를 들면 채팅방별 메시지 조회 등
}
