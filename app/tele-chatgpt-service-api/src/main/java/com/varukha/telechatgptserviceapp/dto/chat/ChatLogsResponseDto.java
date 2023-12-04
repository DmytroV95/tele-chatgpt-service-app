package com.varukha.telechatgptserviceapp.dto.chat;

import lombok.Data;

@Data
public class ChatLogsResponseDto {
    private Long telegramChatId;
    private Long adminId;
    private String message;
    private String messageTime;
    private String messageSource;
}
