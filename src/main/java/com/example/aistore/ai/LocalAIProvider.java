package com.example.aistore.ai;

import com.example.aistore.dto.FeedbackAnalysisDto;
import com.example.aistore.dto.ProductCardDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;

@Component
public class LocalAIProvider implements AIProvider {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LocalAIProvider.class);


    private final String baseUrl;
    private final String model;
    private final double temperature;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final MockAIProvider fallbackProvider;

    public LocalAIProvider(
            @Value("${ai.local.base-url:http://localhost:8000/v1}") String baseUrl,
            @Value("${ai.local.model:nvidia/nemotron-3-nano-30b-a3b}") String model,
            @Value("${ai.local.temperature:0.2}") double temperature,
            ObjectMapper objectMapper,
            MockAIProvider fallbackProvider
    ) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.temperature = temperature;
        this.objectMapper = objectMapper;
        this.fallbackProvider = fallbackProvider;

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public String getProviderName() {
        return "LocalAIProvider (" + model + ")";
    }

    @Override
    public boolean isAvailable() {
        try {
            // Check if local endpoint is reachable with short 1s timeout
            String res = webClient.get()
                    .uri("/models")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(1000))
                    .block();
            return res != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generateChatResponse(String systemPrompt, String userMessage, List<Map<String, String>> conversationHistory) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            if (conversationHistory != null) messages.addAll(conversationHistory);
            messages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", messages,
                    "temperature", temperature
            );

            String responseJson = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            JsonNode root = objectMapper.readTree(responseJson);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.debug("Local AI failed: {}, using fallback mock", e.getMessage());
            return fallbackProvider.generateChatResponse(systemPrompt, userMessage, conversationHistory);
        }
    }

    @Override
    public FeedbackAnalysisDto analyzeCustomerFeedback(String reviewTitle, String reviewComment, int rating) {
        return fallbackProvider.analyzeCustomerFeedback(reviewTitle, reviewComment, rating);
    }

    @Override
    public String generateProductComparisonSummary(List<ProductCardDto> products, Map<String, Map<Long, String>> specMatrix) {
        return fallbackProvider.generateProductComparisonSummary(products, specMatrix);
    }

    @Override
    public Map<String, Object> parseNaturalLanguageSearch(String searchQuery) {
        return fallbackProvider.parseNaturalLanguageSearch(searchQuery);
    }

    @Override
    public String answerAdminQuery(String adminQuestion, String analyticsContext) {
        return fallbackProvider.answerAdminQuery(adminQuestion, analyticsContext);
    }
}
