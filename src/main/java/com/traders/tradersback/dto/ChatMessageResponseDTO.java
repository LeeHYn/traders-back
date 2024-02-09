package com.traders.tradersback.dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {

    private String senderName;
    private String otherPartyName;
    private String message;
    private Long chatRoomId;
    private LocalDateTime timestamp;

    // 생성자
    public ChatMessageResponseDTO(String senderName, String otherPartyName, String message, Long chatRoomId, LocalDateTime timestamp) {
        this.senderName = senderName;
        this.otherPartyName = otherPartyName;
        this.message = message;
        this.chatRoomId = chatRoomId;
        this.timestamp = timestamp;
    }

    // 게터 메소드 추가
    public String getSenderName() {
        return senderName;
    }

    public String getOtherPartyName() {
        return otherPartyName;
    }

    public String getMessage() {
        return message;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
