package com.traders.tradersback.controller;

import com.traders.tradersback.dto.ChatRoomCreateDTO;
import com.traders.tradersback.model.ChatRoom;
import com.traders.tradersback.service.ChatRoomService;
import com.traders.tradersback.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomCreateDTO chatRoomDTO) {
        // 상품 상태 확인 로직
        if (!productService.isAvailableForChat(chatRoomDTO.getProductId())) {
            return ResponseEntity.badRequest().body("Product is not available for chat");
        }

        // 채팅방 생성 로직
        ChatRoom chatRoom = chatRoomService.createOrGetChatRoom(chatRoomDTO.getSellerId(), chatRoomDTO.getBuyerId());
        return ResponseEntity.ok(chatRoom);
    }
}
