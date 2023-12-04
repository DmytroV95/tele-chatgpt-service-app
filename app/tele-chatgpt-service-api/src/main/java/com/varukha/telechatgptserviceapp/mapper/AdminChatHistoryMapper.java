package com.varukha.telechatgptserviceapp.mapper;

import com.varukha.telechatgptserviceapp.config.MapperConfig;
import com.varukha.telechatgptserviceapp.dto.chat.ChatLogsResponseDto;
import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AdminChatHistoryMapper {
    @Mapping(target = "adminId", source = "user.id")
    ChatLogsResponseDto toChatLogsDto(AdminChatHistory chatHistory);
}
