package com.session02ex05.dto;


import jakarta.validation.constraints.NotBlank;

public class WishRequest {

    @NotBlank(message = "Nội dung điều ước không được để trống")
    private String content;

    public WishRequest() {
    }

    public WishRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
