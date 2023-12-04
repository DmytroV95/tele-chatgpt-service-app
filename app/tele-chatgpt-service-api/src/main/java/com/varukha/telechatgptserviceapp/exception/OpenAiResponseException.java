package com.varukha.telechatgptserviceapp.exception;

public class OpenAiResponseException extends RuntimeException {
    public OpenAiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
