package com.traders.tradersback.model;

public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    // 게터와 세터
}
