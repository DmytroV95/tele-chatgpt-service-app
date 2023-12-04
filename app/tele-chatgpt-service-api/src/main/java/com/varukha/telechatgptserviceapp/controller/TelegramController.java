package com.varukha.telechatgptserviceapp.controller;

import com.varukha.telechatgptserviceapp.dto.telegram.CreateMessageRequestDto;
import com.varukha.telechatgptserviceapp.service.TelegramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping(value = "/telegram")
@Tag(name = "Telegram management",
        description = "Endpoints for managing telegram operations")
public class TelegramController {
    private final TelegramService telegramService;

    @PostMapping("/sendMessage")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Send message to Telegram",
            description = "Send a message to Telegram for a user")
    public void sendMessageToTelegram(
            @RequestBody @Valid CreateMessageRequestDto requestMessage) {
        telegramService.sendMessageFromAdminToTelegram(requestMessage);
    }
}
