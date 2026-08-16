package com.example.aistore.ai;

import com.example.aistore.dto.FeedbackAnalysisDto;
import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductComparisonDto;

import java.util.List;
import java.util.Map;

public interface AIProvider {

    String getProviderName();

    boolean isAvailable();

    /**
     * Generate conversational response with reasoning and structured product recommendations.
     */
    String generateChatResponse(String systemPrompt, String userMessage, List<Map<String, String>> conversationHistory);

    /**
     * Analyze customer review text and return structured sentiment, emotions, topics, and specific issues.
     */
    FeedbackAnalysisDto analyzeCustomerFeedback(String reviewTitle, String reviewComment, int rating);

    /**
     * Generate an AI comparison breakdown and reasoning given candidate products and specifications.
     */
    String generateProductComparisonSummary(List<ProductCardDto> products, Map<String, Map<Long, String>> specMatrix);

    /**
     * Natural Language search query parser to structured tool arguments.
     */
    Map<String, Object> parseNaturalLanguageSearch(String searchQuery);

    /**
     * Admin AI Question Answering with factual structured data context.
     */
    String answerAdminQuery(String adminQuestion, String analyticsContext);
}
