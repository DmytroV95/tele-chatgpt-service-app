package com.varukha.telechatgptserviceapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.varukha.telechatgptserviceapp.dto.chat.ChatLogsResponseDto;
import com.varukha.telechatgptserviceapp.mapper.AdminChatHistoryMapper;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import com.varukha.telechatgptserviceapp.model.Role;
import com.varukha.telechatgptserviceapp.model.User;
import com.varukha.telechatgptserviceapp.repository.AdminChatHistoryRepository;
import com.varukha.telechatgptserviceapp.service.impl.AdminChatLogsServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminChatLogsServiceImplTest {
    private static final List<AdminChatHistory> ADMIN_CHAT_HISTORY_LIST =
            new ArrayList<>();
    private static final List<ChatLogsResponseDto> CHAT_LOGS_RESPONSE_DTO_LIST =
            new ArrayList<>();
    private static final AdminChatHistory ADMIN_CHAT_HISTORY_1 =
            new AdminChatHistory();
    private static final AdminChatHistory ADMIN_CHAT_HISTORY_2 =
            new AdminChatHistory();
    private static final ChatLogsResponseDto CHAT_LOGS_RESPONSE_DTO_1 =
            new ChatLogsResponseDto();
    private static final ChatLogsResponseDto CHAT_LOGS_RESPONSE_DTO_2 =
            new ChatLogsResponseDto();
    private static final Role ROLE_ADMIN = new Role();
    private static final User TEST_ACTIVE_ADMIN = new User();

    @Mock
    private AdminChatHistoryRepository adminChatHistoryRepository;
    @Mock
    private AdminChatHistoryMapper adminChatHistoryMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private AdminChatLogsServiceImpl adminChatLogsService;

    @BeforeAll
    public static void setUp() {
        ROLE_ADMIN.setId(1L);
        ROLE_ADMIN.setRoleName(Role.RoleName.ROLE_ADMIN);

        TEST_ACTIVE_ADMIN.setId(1L);
        TEST_ACTIVE_ADMIN.setEmail("test@email.com");
        TEST_ACTIVE_ADMIN.setPassword("testPassword");
        TEST_ACTIVE_ADMIN.setFirstName("testFirstName");
        TEST_ACTIVE_ADMIN.setLastName("testLastName");
        TEST_ACTIVE_ADMIN.setIsActive(true);
        TEST_ACTIVE_ADMIN.setRoles(Set.of(ROLE_ADMIN));

        ADMIN_CHAT_HISTORY_1.setId(1L);
        ADMIN_CHAT_HISTORY_1.setTelegramChatId(123L);
        ADMIN_CHAT_HISTORY_1.setMessage("Hello, tell me please");
        ADMIN_CHAT_HISTORY_1.setMessageTime("22-11-2023 10:27");
        ADMIN_CHAT_HISTORY_1.setMessageSource(Role.RoleName.ROLE_CLIENT.name());

        ADMIN_CHAT_HISTORY_2.setId(2L);
        ADMIN_CHAT_HISTORY_2.setTelegramChatId(123L);
        ADMIN_CHAT_HISTORY_2.setMessage("Hello, tell me please");
        ADMIN_CHAT_HISTORY_2.setMessageTime("22-11-2023 10:28");
        ADMIN_CHAT_HISTORY_2.setMessageSource(Role.RoleName.ROLE_CLIENT.name());

        ADMIN_CHAT_HISTORY_LIST.add(ADMIN_CHAT_HISTORY_1);
        ADMIN_CHAT_HISTORY_LIST.add(ADMIN_CHAT_HISTORY_2);

        CHAT_LOGS_RESPONSE_DTO_1.setTelegramChatId(ADMIN_CHAT_HISTORY_1.getTelegramChatId());
        CHAT_LOGS_RESPONSE_DTO_1.setMessage(ADMIN_CHAT_HISTORY_1.getMessage());
        CHAT_LOGS_RESPONSE_DTO_1.setMessageTime(ADMIN_CHAT_HISTORY_1.getMessageTime());
        CHAT_LOGS_RESPONSE_DTO_1.setMessageSource(ADMIN_CHAT_HISTORY_1.getMessageSource());

        CHAT_LOGS_RESPONSE_DTO_2.setTelegramChatId(ADMIN_CHAT_HISTORY_2.getTelegramChatId());
        CHAT_LOGS_RESPONSE_DTO_2.setMessage(ADMIN_CHAT_HISTORY_2.getMessage());
        CHAT_LOGS_RESPONSE_DTO_2.setMessageTime(ADMIN_CHAT_HISTORY_2.getMessageTime());
        CHAT_LOGS_RESPONSE_DTO_2.setMessageSource(ADMIN_CHAT_HISTORY_2.getMessageSource());

        CHAT_LOGS_RESPONSE_DTO_LIST.add(CHAT_LOGS_RESPONSE_DTO_1);
        CHAT_LOGS_RESPONSE_DTO_LIST.add(CHAT_LOGS_RESPONSE_DTO_2);
    }

    @Test
    @DisplayName("""
            Test 'getAllUserAdminChatLogs' method to retrieve
            all user-admin chat logs
            """)
    void getAllUserAdminChatLogs_ReturnListOfChatLogsResponseDto() {
        when(userService.getAuthenticatedUser()).thenReturn(TEST_ACTIVE_ADMIN);
        when(adminChatHistoryRepository.findAllByUserId(TEST_ACTIVE_ADMIN.getId()))
                .thenReturn(ADMIN_CHAT_HISTORY_LIST);
        when(adminChatHistoryMapper.toChatLogsDto(any()))
                .thenAnswer(invocation -> {
                    AdminChatHistory history = invocation.getArgument(0);
                    return createUserAdminChatHistory(
                            history.getTelegramChatId(),
                            history.getMessage(),
                            history.getMessageTime(),
                            history.getMessageSource());
                });

        List<ChatLogsResponseDto> actualResult = adminChatLogsService.getAllUserAdminChatLogs();

        assertEquals(CHAT_LOGS_RESPONSE_DTO_LIST, actualResult);
    }

    @Test
    @DisplayName("""
            Test 'createUserAdminChatHistory' method to create a new user admin chat history entry
            """)
    void createUserAdminChatHistory_ReturnAdminChatHistory() {
        Long chatId = ADMIN_CHAT_HISTORY_1.getId();
        String message = ADMIN_CHAT_HISTORY_1.getMessage();
        String messageSource = ADMIN_CHAT_HISTORY_1.getMessageSource();

        when(userService.getActiveAdmin()).thenReturn(TEST_ACTIVE_ADMIN);

        AdminChatHistory result = adminChatLogsService.createUserAdminChatHistory(chatId, message, Role.RoleName.valueOf(messageSource));

        assertNotNull(result);
        assertEquals(chatId, result.getTelegramChatId());
        assertEquals(TEST_ACTIVE_ADMIN, result.getUser());
        assertEquals(message, result.getMessage());
        assertNotNull(result.getMessageTime());
        assertEquals(messageSource, result.getMessageSource());
    }

    private ChatLogsResponseDto createUserAdminChatHistory(Long chatId,
                                                           String message,
                                                           String messageTime,
                                                           String messageSource) {
        ChatLogsResponseDto responseDto = new ChatLogsResponseDto();
        responseDto.setTelegramChatId(chatId);
        responseDto.setMessage(message);
        responseDto.setMessageTime(messageTime);
        responseDto.setMessageSource(messageSource);
        return responseDto;
    }
}
