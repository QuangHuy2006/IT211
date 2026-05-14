package com.session02ex05.exception;

public class WishLimitExceededException extends RuntimeException {

    public WishLimitExceededException(String message) {
        super(message);
    }
}
