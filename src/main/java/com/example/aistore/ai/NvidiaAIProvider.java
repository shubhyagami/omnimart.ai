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
public class NvidiaAIProvider implements AIProvider {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NvidiaAIProvider.class);

    private final List<String> apiKeys;
    private final String baseUrl;
    private final String model;
    private final double temperature;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final MockAIProvider fallbackProvider;

    public NvidiaAIProvider(
            @Value("${ai.nvidia.api-keys:}") String apiKeysConfig,
            @Value("${ai.nvidia.api-key:}") String singleApiKey,
            @Value("${ai.nvidia.base-url:https://integrate.api.nvidia.com/v1}") String baseUrl,
            @Value("${ai.nvidia.model:nvidia/nemotron-3-ultra-550b-a55b}") String model,
            @Value("${ai.nvidia.temperature:0.2}") double temperature,
            ObjectMapper objectMapper,
            MockAIProvider fallbackProvider
    ) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.temperature = temperature;
        this.objectMapper = objectMapper;
        this.fallbackProvider = fallbackProvider;

        List<String> keys = new ArrayList<>();
        if (apiKeysConfig != null && !apiKeysConfig.trim().isEmpty()) {
            String[] split = apiKeysConfig.split("[,\\r\\n]+");
            for (String k : split) {
                String trimmed = k.trim();
                if (!trimmed.isEmpty() && !keys.contains(trimmed)) {
                    keys.add(trimmed);
                }
            }
        }
        if (singleApiKey != null && !singleApiKey.trim().isEmpty() && !keys.contains(singleApiKey.trim())) {
            keys.add(singleApiKey.trim());
        }

        this.apiKeys = Collections.unmodifiableList(keys);

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("Initialized NvidiaAIProvider with {} API key(s) in fallback pool for model '{}'", this.apiKeys.size(), model);
    }

    @Override
    public String getProviderName() {
        return "NvidiaAIProvider (" + model + " - " + apiKeys.size() + " key pool)";
    }

    @Override
    public boolean isAvailable() {
        return !apiKeys.isEmpty() && apiKeys.stream().anyMatch(k -> !k.contains("your_nvidia_api_key"));
    }

    public List<String> getApiKeys() {
        return apiKeys;
    }

    /**
     * Executes the HTTP completion request with sequential fallback across all configured API keys.
     */
    private String executeWithFailover(Map<String, Object> requestBody) throws Exception {
        if (apiKeys.isEmpty()) {
            throw new IllegalStateException("No NVIDIA API keys configured in pool.");
        }

        Exception lastException = null;
        for (int i = 0; i < apiKeys.size(); i++) {
            String key = apiKeys.get(i);
            try {
                String maskedKey = key.length() > 12 ? key.substring(0, 8) + "..." + key.substring(key.length() - 4) : "***";
                log.debug("Invoking NVIDIA API using key index [{}/{}]: {}", (i + 1), apiKeys.size(), maskedKey);

                String responseJson = webClient.post()
                        .uri("/chat/completions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofMillis(2500))
                        .block();

                if (responseJson != null && !responseJson.trim().isEmpty()) {
                    if (i > 0) {
                        log.info("Successfully recovered request using fallback NVIDIA API key [{}/{}]", (i + 1), apiKeys.size());
                    }
                    return responseJson;
                }
            } catch (Exception e) {
                lastException = e;
                log.warn("NVIDIA API key [{}/{}] failed with message: {}. Attempting fallback to next key...", 
                        (i + 1), apiKeys.size(), e.getMessage());
            }
        }

        throw (lastException != null ? lastException : new RuntimeException("All " + apiKeys.size() + " NVIDIA API keys exhausted."));
    }

    @Override
    public String generateChatResponse(String systemPrompt, String userMessage, List<Map<String, String>> conversationHistory) {
        if (!isAvailable()) {
            return fallbackProvider.generateChatResponse(systemPrompt, userMessage, conversationHistory);
        }

        try {
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            if (conversationHistory != null) {
                messages.addAll(conversationHistory);
            }
            messages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", messages,
                    "temperature", temperature,
                    "max_tokens", 2048
            );

            String responseJson = executeWithFailover(requestBody);

            JsonNode root = objectMapper.readTree(responseJson);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.warn("All NVIDIA API keys failed or timed out. Gracefully falling back to deterministic engine: {}", e.getMessage());
            return fallbackProvider.generateChatResponse(systemPrompt, userMessage, conversationHistory);
        }
    }

    @Override
    public FeedbackAnalysisDto analyzeCustomerFeedback(String reviewTitle, String reviewComment, int rating) {
        if (!isAvailable()) {
            return fallbackProvider.analyzeCustomerFeedback(reviewTitle, reviewComment, rating);
        }

        try {
            String prompt = String.format("""
                Analyze this e-commerce customer review:
                Rating: %d/5
                Title: %s
                Review: %s

                Return ONLY valid JSON with keys:
                {
                   "sentiment": "Positive" | "Negative" | "Mixed" | "Neutral",
                   "emotion": "Satisfied" | "Frustrated" | "Disappointed" | "Delighted" | "Neutral",
                   "primaryTopic": "Battery" | "Display" | "Camera" | "Performance" | "Build Quality" | "Delivery" | "General",
                   "specificIssues": ["issue 1", "issue 2"],
                   "positiveAspects": ["positive 1", "positive 2"],
                   "confidenceScore": 0.95
                }
                """, rating, reviewTitle, reviewComment);

            String response = generateChatResponse("You are an expert customer feedback intelligence NLP model. Return strictly valid JSON.", prompt, null);
            String cleanedJson = response.replaceAll("```json", "").replaceAll("```", "").trim();
            return objectMapper.readValue(cleanedJson, FeedbackAnalysisDto.class);
        } catch (Exception e) {
            log.warn("NVIDIA feedback analysis fallback triggered: {}", e.getMessage());
            return fallbackProvider.analyzeCustomerFeedback(reviewTitle, reviewComment, rating);
        }
    }

    @Override
    public String generateProductComparisonSummary(List<ProductCardDto> products, Map<String, Map<Long, String>> specMatrix) {
        if (!isAvailable()) {
            return fallbackProvider.generateProductComparisonSummary(products, specMatrix);
        }
        try {
            StringBuilder specText = new StringBuilder();
            for (ProductCardDto p : products) {
                specText.append("Product: ").append(p.getName()).append(" Price: ₹").append(p.getPrice()).append("\n");
            }
            String prompt = "Compare the following candidate products accurately based strictly on their provided values:\n" + specText;
            return generateChatResponse("You are an impartial hardware specialist and product analyst. Compare products concisely and factually without inventing features.", prompt, null);
        } catch (Exception e) {
            return fallbackProvider.generateProductComparisonSummary(products, specMatrix);
        }
    }

    @Override
    public Map<String, Object> parseNaturalLanguageSearch(String searchQuery) {
        return fallbackProvider.parseNaturalLanguageSearch(searchQuery);
    }

    @Override
    public String answerAdminQuery(String adminQuestion, String analyticsContext) {
        if (!isAvailable()) {
            return fallbackProvider.answerAdminQuery(adminQuestion, analyticsContext);
        }
        try {
            String prompt = "Factual store analytics context:\n" + analyticsContext + "\n\nAdmin question: " + adminQuestion;
            return generateChatResponse("You are an executive e-commerce AI analytics consultant. Base answers strictly on facts from context.", prompt, null);
        } catch (Exception e) {
            return fallbackProvider.answerAdminQuery(adminQuestion, analyticsContext);
        }
    }
}
