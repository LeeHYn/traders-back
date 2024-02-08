package com.traders.tradersback.controller;

import com.traders.tradersback.dto.ChatRoomCreateDTO;
import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.service.ChatRoomService;
import com.traders.tradersback.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {



    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomController.class);


    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomCreateDTO chatRoomDTO) {
        logger.info("Creating chat room with data: {}", chatRoomDTO);

        if (!productService.isAvailableForChat(chatRoomDTO.getProductId())) {
            return ResponseEntity.badRequest().body("Product is not available for chat");
        }

        chatRoomDTO.setStatus("거래중");

        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(chatRoomDTO.getSellerId(), chatRoomDTO.getBuyerId(), chatRoomDTO.getProductId(), chatRoomDTO.getStatus());
        return ResponseEntity.ok(chatRoom);
    }


    @PatchMapping("/update/{chatRoomId}/status")
    public ResponseEntity<?> updateChatRoomStatus(@PathVariable Long chatRoomId, @RequestBody String status) {
        try {
            chatRoomService.updateChatRoomStatus(chatRoomId, status);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 채팅방 정보 조회 API
    @GetMapping("/room/{chatRoomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable Long chatRoomId) {
        try {
            ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
            return ResponseEntity.ok(chatRoom);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
}

