package com.traders.tradersback.dto;

public class ChatMessageDTO {

    private String message;
    private String senderName; // 사용자 이름을 담는 필드 추가
    private Long chatRoomId;

    // 기본 생성자
    public ChatMessageDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public ChatMessageDTO(String message, String senderName, Long chatRoomId) {
        this.message = message;
        this.senderName = senderName;
        this.chatRoomId = chatRoomId;
    }

    // Getter와 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    // toString 메서드는 로깅 및 디버깅에 유용
    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "message='" + message + '\'' +
                ", senderName='" + senderName + '\'' +
                ", chatRoomId=" + chatRoomId +
                '}';
    }
}
