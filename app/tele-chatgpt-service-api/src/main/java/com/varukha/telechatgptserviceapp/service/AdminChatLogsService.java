package com.varukha.telechatgptserviceapp.service;

import com.varukha.telechatgptserviceapp.dto.chat.ChatLogsResponseDto;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import com.varukha.telechatgptserviceapp.model.Role;
import java.util.List;

public interface AdminChatLogsService {
    List<ChatLogsResponseDto> getAllUserAdminChatLogs();

    List<ChatLogsResponseDto> getUserAdminChatLogsById(Long id);

    AdminChatHistory createUserAdminChatHistory(Long chatId,
                                                String message,
                                                Role.RoleName messageSource);
}
