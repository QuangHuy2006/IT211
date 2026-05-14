package com.session02ex05.model;

import java.time.LocalDateTime;

public class WishHistory {

    private String wishType;
    private String content;
    private String status;
    private String resultMessage;
    private LocalDateTime createdAt;

    public WishHistory() {
    }

    public WishHistory(String wishType, String content, String status, String resultMessage) {
        this.wishType = wishType;
        this.content = content;
        this.status = status;
        this.resultMessage = resultMessage;
        this.createdAt = LocalDateTime.now();
    }

    public String getWishType() {
        return wishType;
    }

    public void setWishType(String wishType) {
        this.wishType = wishType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
