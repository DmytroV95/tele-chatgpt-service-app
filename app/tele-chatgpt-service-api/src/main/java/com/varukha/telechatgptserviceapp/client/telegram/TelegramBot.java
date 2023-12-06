package com.varukha.telechatgptserviceapp.client.telegram;

import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.ASK_ADMIN_BUTTON;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.ASK_ADMIN_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.ASK_CHAT_GPT_BUTTON;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.ASK_CHAT_GPT_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.CHAT_WITH_ADMIN_MESSAGE;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.CHAT_WITH_CHAT_GPT_MESSAGE;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.EXIT_FROM_CHAT;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.GREETING_MESSAGE;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.HELP_BUTTON;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.HELP_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.HELP_MESSAGE;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.INFO_MESSAGE_ASK_ADMIN_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.INFO_MESSAGE_ASK_CHAT_GPT_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.INFO_MESSAGE_HELP_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.INFO_MESSAGE_START_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.START_COMMAND;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.STOP_CHAT_BUTTON;
import static com.varukha.telechatgptserviceapp.client.telegram.TelegramMessages.STOP_CHAT_MESSAGE;

import com.varukha.telechatgptserviceapp.client.chatgpt.OpenAiIntegrationClient;
import com.varukha.telechatgptserviceapp.config.TelegramBotConfig;
import com.varukha.telechatgptserviceapp.service.TelegramService;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final Map<Long, ChatMode> userStates = new HashMap<>();
    private final TelegramBotConfig config;
    private final OpenAiIntegrationClient openAiIntegrationClient;
    private final TelegramService telegramService;

    @PostConstruct
    private void init() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(START_COMMAND, INFO_MESSAGE_START_COMMAND));
        botCommands.add(new BotCommand(HELP_COMMAND, INFO_MESSAGE_HELP_COMMAND));
        botCommands.add(new BotCommand(ASK_CHAT_GPT_COMMAND, INFO_MESSAGE_ASK_CHAT_GPT_COMMAND));
        botCommands.add(new BotCommand(ASK_ADMIN_COMMAND, INFO_MESSAGE_ASK_ADMIN_COMMAND));
        try {
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can`t create a bot menu", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (isHandleMessageToChatGpt(messageText)) {
                handleChatMode(chatId, messageText);
            } else if (isHandleMessageToAdmin(messageText)) {
                handleChatMode(chatId, messageText);
            } else {
                handleDefaultCommands(chatId, messageText);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    private boolean isHandleMessageToChatGpt(String messageText) {
        return userStates.containsValue(ChatMode.CHAT_GPT_MODE)
                && !messageText.equals(STOP_CHAT_BUTTON);
    }

    private boolean isHandleMessageToAdmin(String messageText) {
        return userStates.containsValue(ChatMode.CHAT_TO_ADMIN_MODE)
                && !messageText.equals(STOP_CHAT_BUTTON);
    }

    private void handleDefaultCommands(Long chatId, String messageText) {
        switch (messageText) {
            case START_COMMAND -> startCommand(chatId);
            case ASK_CHAT_GPT_COMMAND, ASK_CHAT_GPT_BUTTON -> searchWithChatGpt(chatId);
            case ASK_ADMIN_COMMAND, ASK_ADMIN_BUTTON -> askQuestionToTheAdmin(chatId);
            case EXIT_FROM_CHAT, STOP_CHAT_BUTTON -> stopChat(chatId);
            default -> showHelpMessage(chatId);
        }
    }

    private void handleChatMode(Long chatId, String messageText) {
        ChatMode currentState = userStates.get(chatId);
        switch (currentState) {
            case CHAT_GPT_MODE -> getChatGptResponse(chatId, messageText);
            case CHAT_TO_ADMIN_MODE -> sendMessageToAdmin(chatId, messageText);
            default -> showHelpMessage(chatId);
        }
    }

    private void getChatGptResponse(Long chatId, String userMessage) {
        String chatGptResponse = openAiIntegrationClient
                .getOpenAiResponse(userMessage);
        sendMessage(chatId, chatGptResponse, showButtons());
    }

    private void sendMessageToAdmin(Long chatId, String userMessage) {
        telegramService.saveUserMessageToAdmin(chatId, userMessage);
    }

    private void searchWithChatGpt(Long chatId) {
        sendMessage(chatId, CHAT_WITH_CHAT_GPT_MESSAGE, showButtons());
        userStates.put(chatId, ChatMode.CHAT_GPT_MODE);
    }

    private void askQuestionToTheAdmin(Long chatId) {
        sendMessage(chatId, CHAT_WITH_ADMIN_MESSAGE, showButtons());
        userStates.put(chatId, ChatMode.CHAT_TO_ADMIN_MODE);
    }

    private void stopChat(Long chatId) {
        if (userStates.containsValue(ChatMode.CHAT_TO_ADMIN_MODE)
                || userStates.containsValue(ChatMode.CHAT_GPT_MODE)) {
            sendMessage(chatId, STOP_CHAT_MESSAGE, showButtons());
            userStates.clear();
        }
    }

    private void startCommand(Long chatId) {
        sendMessage(chatId, GREETING_MESSAGE, showButtons());
        showButtons();
    }

    private void showHelpMessage(Long chatId) {
        sendMessage(chatId, HELP_MESSAGE, showButtons());
    }

    private void sendMessage(Long chatId, String textMessage,
                             ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup showButtons() {
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(ASK_CHAT_GPT_BUTTON);
        firstRow.add(ASK_ADMIN_BUTTON);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(STOP_CHAT_BUTTON);
        secondRow.add(HELP_BUTTON);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    private enum ChatMode {
        CHAT_GPT_MODE,
        CHAT_TO_ADMIN_MODE
    }
}
