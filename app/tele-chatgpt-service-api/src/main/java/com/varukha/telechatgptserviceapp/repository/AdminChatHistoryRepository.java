package com.varukha.telechatgptserviceapp.repository;

import com.varukha.telechatgptserviceapp.model.AdminChatHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminChatHistoryRepository extends JpaRepository<AdminChatHistory, Long> {
    List<AdminChatHistory> findAllByUserId(Long userId);

    List<AdminChatHistory> findByTelegramChatIdAndUserId(Long chatId, Long userId);
}
