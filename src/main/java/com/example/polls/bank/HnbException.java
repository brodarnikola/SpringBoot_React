package com.example.polls.bank;

public class HnbException extends RuntimeException{
    public HnbException(String message) {
        super(message);
    }

    public HnbException(Throwable cause) {
        super(cause);
    }
}