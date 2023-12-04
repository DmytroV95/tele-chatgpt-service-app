package com.varukha.telechatgptserviceapp.service.impl;

import com.varukha.telechatgptserviceapp.client.telegram.TelegramBotMessageSender;
import com.varukha.telechatgptserviceapp.dto.telegram.CreateMessageRequestDto;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
//import com.varukha.telechatgptserviceapp.model.ChatGptHistory;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.repository.AdminChatHistoryRepository;
//import com.varukha.telechatgptserviceapp.repository.ChatGptHistoryRepository;
import com.varukha.telechatgptserviceapp.service.AdminChatLogsService;
//import com.varukha.telechatgptserviceapp.service.ChatGptLogsService;
import com.varukha.telechatgptserviceapp.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramServiceImp implements TelegramService {
    private final TelegramBotMessageSender telegramBotMessageSender;
//    private final ChatGptLogsService chatGptGptLogsService;
    private final AdminChatLogsService adminChatLogsService;
//    private final ChatGptHistoryRepository chatGptHistoryRepository;
    private final AdminChatHistoryRepository adminChatHistoryRepository;

    @Override
    @Transactional
    public void sendMessageFromAdminToTelegram(CreateMessageRequestDto requestDto) {
        AdminChatHistory adminChatHistory = adminChatLogsService
                .createUserAdminChatHistory(requestDto.getChatId(),
                        requestDto.getMessage(), Role.RoleName.ROLE_ADMIN);
        adminChatHistoryRepository.save(adminChatHistory);
        telegramBotMessageSender.sendMessage(requestDto.getChatId(),
                requestDto.getMessage());
    }

    @Override
    public void saveUserMessageToAdmin(Long chatId, String message) {
        AdminChatHistory adminChatHistory = adminChatLogsService
                .createUserAdminChatHistory(chatId, message, Role.RoleName.ROLE_CLIENT);
        adminChatHistoryRepository.save(adminChatHistory);
    }

//    @Override
//    public void saveChatGptResponseMessage(Long chatId, String message) {
//        ChatGptHistory chatGptHistory = chatGptGptLogsService
//                .createChatGptHistory(chatId, message, Role.RoleName.CHAT_GPT);
//        chatGptHistoryRepository.save(chatGptHistory);
//    }

//    @Override
//    public void saveUserMessageToChatGpt(Long chatId, String message) {
//        ChatGptHistory chatGptHistory = chatGptGptLogsService
//                .createChatGptHistory(chatId, message, Role.RoleName.ROLE_CLIENT);
//        chatGptHistoryRepository.save(chatGptHistory);
//    }
}
