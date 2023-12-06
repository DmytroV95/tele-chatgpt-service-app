package com.varukha.telechatgptserviceapp.controller;

import com.varukha.telechatgptserviceapp.dto.chat.ChatLogsResponseDto;
import com.varukha.telechatgptserviceapp.service.AdminChatLogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/history")
@RequiredArgsConstructor
@Tag(name = "ChatLogController",
        description = "Endpoints for managing chat operations")
public class ChatLogsController {
    private final AdminChatLogsService adminChatLogsService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all chat logs",
            description = "Get list of all chats between"
                    + " customer and admin for current user id")
    public List<ChatLogsResponseDto> getAllChatLogs() {
        return adminChatLogsService.getAllUserAdminChatLogs();
    }

    @GetMapping("/chat/id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all chat logs by telegram id",
            description = "Get list of all chats between customer" +
                    " and admin by telegram id")
    public List<ChatLogsResponseDto> getChatLogsByChatId(@RequestParam Long id) {
        return adminChatLogsService.getUserAdminChatLogsById(id);
    }
}
