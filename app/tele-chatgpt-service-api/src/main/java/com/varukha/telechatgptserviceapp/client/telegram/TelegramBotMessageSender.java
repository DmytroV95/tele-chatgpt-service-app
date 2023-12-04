package com.varukha.telechatgptserviceapp.client.telegram;

import com.varukha.telechatgptserviceapp.config.TelegramBotConfig;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramBotMessageSender {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private final TelegramBotConfig config;

    public void sendMessage(Long chatId, String message) {
        String apiUrl = String.format(
                "https://api.telegram.org/bot%s/sendMessage", config.getToken());
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);

            httpPost.setEntity(new StringEntity(buildJsonString(chatId, message)));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                System.out.println("Response Code: " + response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error communicating with TelegramAPI: " + e.getMessage());
        }
    }

    private String buildJsonString(Long chatId, String message) {
        return new StringBuilder()
                .append("{\"chat_id\":\"")
                .append(chatId)
                .append("\",")
                .append("\"text\":\"")
                .append(message)
                .append("\"}")
                .toString();
    }
}
