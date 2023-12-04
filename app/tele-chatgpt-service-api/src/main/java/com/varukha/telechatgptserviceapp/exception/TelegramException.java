package com.varukha.telechatgptserviceapp.exception;

public class TelegramException extends RuntimeException {
    public TelegramException(String message, Throwable cause) {
        super(message, cause);
    }
}
