package com.traders.tradersback.dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {

    private String senderName; // 메시지를 보낸 사람의 이름
    private String otherPartyName; // 메시지의 상대방 이름 (판매자 또는 구매자 중 하나)
    private String message; // 메시지 본문
    private Long chatRoomId; // 채팅방 ID
    private LocalDateTime timestamp; // 메시지 전송 시간

    // 생성자, 게터, 세터...
    public ChatMessageResponseDTO(String senderName, String otherPartyName, String message, Long chatRoomId, LocalDateTime timestamp) {
        this.senderName = senderName;
        this.otherPartyName = otherPartyName;
        this.message = message;
        this.chatRoomId = chatRoomId;
        this.timestamp = timestamp;
    }

    // 생성자, 게터, 세터...




    public void setMessage(String message) {
        this.message = message;
    }



    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }



    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
