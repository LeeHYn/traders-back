package com.traders.tradersback.controller;

import com.traders.tradersback.model.ChatMessage;
import com.traders.tradersback.service.ChatService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        try {
            return chatService.saveMessage(chatMessage);
        } catch (EntityNotFoundException ex) {
            // 채팅방이 존재하지 않는 경우
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다.", ex);
        } catch (Exception ex) {
            // 다른 일반적인 예외 처리
            throw new ServiceException("메시지 전송 중 오류가 발생했습니다.", ex);
        }
    }

    @GetMapping("/room/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        try {
            List<ChatMessage> messages = chatService.getMessagesByChatRoomId(chatRoomId);
            return ResponseEntity.ok(messages);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}