package com.varukha.telechatgptserviceapp.service;

import com.varukha.telechatgptserviceapp.dto.telegram.CreateMessageRequestDto;

public interface TelegramService {
    void sendMessageFromAdminToTelegram(CreateMessageRequestDto message);

    void saveUserMessageToAdmin(Long chatId, String messageFromUser);
}
