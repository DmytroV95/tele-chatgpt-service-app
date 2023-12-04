package com.varukha.telechatgptserviceapp.client.chatgpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.varukha.telechatgptserviceapp.config.OpenAiConfig;
import com.varukha.telechatgptserviceapp.exception.OpenAiResponseException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiIntegrationClient {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int CHAT_GPT_TOKEN_RESPONSE_QTY = 150;

    private final ObjectMapper objectMapper;
    private final OpenAiConfig config;

    public String getOpenAiResponse(String userMessage) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String requestBody = createRequestBody(userMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getOpenAiUrl()))
                .header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + config.getOpenApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());
            return extractResponse(response.body());
        } catch (IOException | InterruptedException e) {
            throw new OpenAiResponseException("Error communicating with OpenAI API", e);
        }
    }

    private String createRequestBody(String userMessage) {
        return new StringBuilder()
                .append("{")
                .append("\"model\":\"")
                .append(config.getChatGptModel())
                .append("\",")
                .append("\"messages\":[{\"role\":\"user\",\"content\":\"")
                .append(userMessage)
                .append("\"}],")
                .append("\"max_tokens\":" + CHAT_GPT_TOKEN_RESPONSE_QTY)
                .append("}")
                .toString();
    }

    private String extractResponse(String responseBody) {
        try {
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            return jsonResponse
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (IOException e) {
            throw new OpenAiResponseException("Error parsing OpenAI API response", e);
        }
    }
}
