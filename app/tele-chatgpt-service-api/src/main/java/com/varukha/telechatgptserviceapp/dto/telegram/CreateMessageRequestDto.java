package com.varukha.telechatgptserviceapp.dto.telegram;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMessageRequestDto {
    @NotNull
    private Long chatId;
    @NotBlank
    private String message;
}
