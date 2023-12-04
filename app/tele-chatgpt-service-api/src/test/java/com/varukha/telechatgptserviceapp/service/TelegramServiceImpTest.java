package com.varukha.telechatgptserviceapp.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.varukha.telechatgptserviceapp.client.telegram.TelegramBotMessageSender;
import com.varukha.telechatgptserviceapp.dto.telegram.CreateMessageRequestDto;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.repository.AdminChatHistoryRepository;
import com.varukha.telechatgptserviceapp.service.impl.TelegramServiceImp;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelegramServiceImpTest {
    private static final CreateMessageRequestDto MESSAGE_REQUEST_DTO =
            new CreateMessageRequestDto();
    private static final AdminChatHistory ADMIN_CHAT_HISTORY =
            new AdminChatHistory();
    private static final User TEST_USER = new User();
    private static final Role ROLE_ADMIN = new Role();

    @Mock
    private TelegramBotMessageSender telegramBotMessageSender;
    @Mock
    private AdminChatLogsService adminChatLogsService;
    @Mock
    private AdminChatHistoryRepository adminChatHistoryRepository;
    @InjectMocks
    private TelegramServiceImp telegramService;

    @BeforeAll
    public static void setUp() {
        ROLE_ADMIN.setId(1L);
        ROLE_ADMIN.setRoleName(Role.RoleName.ROLE_ADMIN);

        TEST_USER.setId(1L);
        TEST_USER.setEmail("test@email.com");
        TEST_USER.setPassword("testPassword");
        TEST_USER.setFirstName("testFirstName");
        TEST_USER.setLastName("testLastName");
        TEST_USER.setIsActive(true);
        TEST_USER.setRoles(Set.of(ROLE_ADMIN));

        MESSAGE_REQUEST_DTO.setChatId(123L);
        MESSAGE_REQUEST_DTO.setMessage("Hello, Telegram!");

        ADMIN_CHAT_HISTORY.setId(1L);
        ADMIN_CHAT_HISTORY.setTelegramChatId(123L);
        ADMIN_CHAT_HISTORY.setUser(TEST_USER);
        ADMIN_CHAT_HISTORY.setMessage("How can I hep you?");
        ADMIN_CHAT_HISTORY.setMessageTime("22-11-2023 00:27");
        ADMIN_CHAT_HISTORY.setMessageSource(
                Role.RoleName.ROLE_ADMIN.name());
    }

    @Test
    @DisplayName("""
            Test 'sendMessageFromAdminToTelegram' method to send message from
            admin UI to client telegram bot
            """)
    void sendMessageFromAdminToTelegram() {
        doReturn(ADMIN_CHAT_HISTORY)
                .when(adminChatLogsService)
                .createUserAdminChatHistory(
                        eq(MESSAGE_REQUEST_DTO.getChatId()),
                        eq(MESSAGE_REQUEST_DTO.getMessage()),
                        eq(ROLE_ADMIN.getRoleName())
                );

        telegramService.sendMessageFromAdminToTelegram(MESSAGE_REQUEST_DTO);

        verify(adminChatHistoryRepository).save(ADMIN_CHAT_HISTORY);
        verify(telegramBotMessageSender).sendMessage(
                eq(MESSAGE_REQUEST_DTO.getChatId()),
                eq(MESSAGE_REQUEST_DTO.getMessage())
        );
    }

    @Test
    @DisplayName("""
            Test 'saveUserMessageToAdmin' method to save message to save
            conversation between admin and client to db
            """)
    void saveUserMessageToAdmin() {
        when(adminChatLogsService.createUserAdminChatHistory(MESSAGE_REQUEST_DTO.getChatId(),
                MESSAGE_REQUEST_DTO.getMessage(), Role.RoleName.ROLE_CLIENT))
                .thenReturn(ADMIN_CHAT_HISTORY);

        telegramService.saveUserMessageToAdmin(MESSAGE_REQUEST_DTO.getChatId(),
                MESSAGE_REQUEST_DTO.getMessage());

        verify(adminChatHistoryRepository).save(ADMIN_CHAT_HISTORY);
    }
}
