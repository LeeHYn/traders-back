package com.traders.tradersback.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatmessage")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_num")
    private Long id;

    @Column(name = "chat_room_num")
    private Long chatRoomId;

    @Column(name = "sender_num")
    private Long senderId;

    @Column
    private String message;

    @Column
    private LocalDateTime timestamp;

    // 게터와 세터

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
