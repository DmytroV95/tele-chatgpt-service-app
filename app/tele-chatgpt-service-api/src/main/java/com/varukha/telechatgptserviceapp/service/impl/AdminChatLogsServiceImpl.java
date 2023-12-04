package com.varukha.telechatgptserviceapp.service.impl;

import com.varukha.telechatgptserviceapp.dto.chat.ChatLogsResponseDto;
import com.varukha.telechatgptserviceapp.mapper.AdminChatHistoryMapper;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.repository.AdminChatHistoryRepository;
import com.varukha.telechatgptserviceapp.service.AdminChatLogsService;
import com.varukha.telechatgptserviceapp.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminChatLogsServiceImpl implements AdminChatLogsService {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final AdminChatHistoryMapper adminChatHistoryMapper;
    private final AdminChatHistoryRepository adminChatHistoryRepository;
    private final UserService userService;

    @Override
    public List<ChatLogsResponseDto> getAllUserAdminChatLogs() {
        User authenticatedUser = userService.getAuthenticatedUser();
        return adminChatHistoryRepository.findAllByUserId(authenticatedUser.getId())
                .stream()
                .map(adminChatHistoryMapper::toChatLogsDto)
                .toList();
    }

    @Override
    public List<ChatLogsResponseDto> getUserAdminChatLogsById(Long id) {
        User authenticatedUser = userService.getAuthenticatedUser();
        return adminChatHistoryRepository
                .findByTelegramChatIdAndUserId(id, authenticatedUser.getId())
                .stream()
                .map(adminChatHistoryMapper::toChatLogsDto)
                .toList();
    }

    @Override
    public AdminChatHistory createUserAdminChatHistory(Long chatId,
                                                       String message,
                                                       Role.RoleName messageSource) {
        User activeAdmin = userService.getActiveAdmin();
        AdminChatHistory adminChatHistory = new AdminChatHistory();
        adminChatHistory.setTelegramChatId(chatId);
        adminChatHistory.setUser(activeAdmin);
        adminChatHistory.setMessage(message);
        adminChatHistory.setMessageTime(LocalDateTime.now().format(FORMATTER));
        adminChatHistory.setMessageSource(messageSource.name());
        return adminChatHistory;
    }
}
