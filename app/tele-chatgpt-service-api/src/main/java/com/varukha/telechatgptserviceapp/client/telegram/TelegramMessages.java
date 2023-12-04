package com.varukha.telechatgptserviceapp.client.telegram;

public class TelegramMessages {
    public static final String HELP_MESSAGE = """ 
            You can use menu or buttons to use this bot. \n
            Here is a list of available features. \n
            1. /start or Start button: Getting started with the bot. \n
            2. /search or Ask ChatGPT button: Help clients
                to communicate with chatGPT \n
            3. /ask or Ask Admin button: Help clients to get information
                about services from admin \n
            4. /stop or Stop chat button: Stop communication
                with chatGpt or admin \n
            5. /help or Help button: Get information
                about bot functions \n""";
    public static final String GREETING_MESSAGE = """
            Hi, nice to meet you! \n
            Welcome to the TeleChatGptServiceBot. \n
            This bot will help you get the necessary information about our services. \n
            Also you can use ChatGPT to find out something interesting \n
            for you or directly ask the admin.
            """;
    public static final String STOP_CHAT_MESSAGE = "Your communication has been stopped";
    public static final String CHAT_WITH_ADMIN_MESSAGE =
            "Now you can ask question to the admin. Please enter your message";
    public static final String CHAT_WITH_CHAT_GPT_MESSAGE =
            "Now you can interact with ChatGPT. Please enter your message";
    public static final String ASK_CHAT_GPT_BUTTON = "Ask ChatGPT";
    public static final String ASK_ADMIN_BUTTON = "Ask Admin";
    public static final String STOP_CHAT_BUTTON = "Stop chat";
    public static final String HELP_BUTTON = "Help";
    public static final String HELP_COMMAND = "/help";
    public static final String START_COMMAND = "/start";
    public static final String ASK_CHAT_GPT_COMMAND = "/message_to_chat_gpt";
    public static final String ASK_ADMIN_COMMAND = "/message_to_admin";
    public static final String EXIT_FROM_CHAT = "/stop_chat";
    public static final String INFO_MESSAGE_START_COMMAND = "Start working with the bot";
    public static final String INFO_MESSAGE_HELP_COMMAND = "Instructions for use";
    public static final String INFO_MESSAGE_ASK_CHAT_GPT_COMMAND = "Search information with ChatGPT";
    public static final String INFO_MESSAGE_ASK_ADMIN_COMMAND = "Get information from admin";
}
