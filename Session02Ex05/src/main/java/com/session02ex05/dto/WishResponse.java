package com.session02ex05.dto;

public class WishResponse {

    private String message;
    private int remainingWishes;

    public WishResponse() {
    }

    public WishResponse(String message, int remainingWishes) {
        this.message = message;
        this.remainingWishes = remainingWishes;
    }

    public String getMessage() {
        return message;
    }

    public int getRemainingWishes() {
        return remainingWishes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRemainingWishes(int remainingWishes) {
        this.remainingWishes = remainingWishes;
    }
}
