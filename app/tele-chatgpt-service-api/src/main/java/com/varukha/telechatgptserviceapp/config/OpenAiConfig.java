package com.varukha.telechatgptserviceapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OpenAiConfig {
    @Value("${openai.api.key}")
    private String openApiKey;

    @Value("${openai.api.url}")
    private String openAiUrl;

    @Value("${openai.api.model}")
    private String chatGptModel;
}
