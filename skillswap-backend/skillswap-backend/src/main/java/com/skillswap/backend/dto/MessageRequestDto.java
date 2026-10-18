package com.skillswap.backend.dto;

public class MessageRequestDto {

    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String messageText;

    public MessageRequestDto() {
    }

    public MessageRequestDto(Long senderId, Long receiverId, String senderName, String messageText) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.messageText = messageText;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
