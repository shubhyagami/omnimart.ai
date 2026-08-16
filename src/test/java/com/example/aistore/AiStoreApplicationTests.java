package com.example.aistore;

import com.example.aistore.ai.AIOrchestrator;
import com.example.aistore.ai.MockAIProvider;
import com.example.aistore.ai.ToolRouter;
import com.example.aistore.dto.ChatRequest;
import com.example.aistore.dto.ChatResponse;
import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.service.HybridRecommendationService;
import com.example.aistore.service.ProductComparisonService;
import com.example.aistore.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AiStoreApplicationTests {

    @Autowired
    private MockAIProvider mockAIProvider;

    @Autowired
    private ToolRouter toolRouter;

    @Autowired
    private AIOrchestrator aiOrchestrator;

    @Autowired
    private ProductService productService;

    @Autowired
    private HybridRecommendationService recommendationService;

    @Autowired
    private ProductComparisonService comparisonService;

    @Autowired
    private com.example.aistore.ai.NvidiaAIProvider nvidiaAIProvider;

    @Test
    @DisplayName("Test Multi-Key NVIDIA API Fallback Pool Configuration")
    void testNvidiaMultiKeyFallbackPool() {
        assertNotNull(nvidiaAIProvider);
        List<String> keys = nvidiaAIProvider.getApiKeys();
        assertNotNull(keys);
        assertTrue(keys.size() >= 3, "All 3 fallback API keys should be loaded into the provider pool");
        assertTrue(nvidiaAIProvider.isAvailable());
        assertTrue(keys.get(0).startsWith("nvapi-0rl4RNeT323"));
        assertTrue(keys.get(1).startsWith("nvapi-8ObJAmgUbhv"));
        assertTrue(keys.get(2).startsWith("nvapi-3bObcrAihiN"));
    }

    @Test
    @DisplayName("Test Natural Language Constraint Extraction")
    void testNaturalLanguageExtraction() {
        String query = "Show me a gaming laptop under ₹80,000 with 16GB RAM and top rating";
        Map<String, Object> extracted = mockAIProvider.parseNaturalLanguageSearch(query);

        assertNotNull(extracted);
        assertEquals("Laptops", extracted.get("category"));
        assertEquals(BigDecimal.valueOf(80000), extracted.get("maxPrice"));
        assertEquals(4.0, extracted.get("minRating"));
    }

    @Test
    @DisplayName("Test AI Tool Router Search Execution")
    void testToolRouterSearch() {
        Map<String, Object> params = Map.of(
                "category", "Laptops",
                "maxPrice", new BigDecimal("90000"),
                "minRating", 4.0
        );

        List<ProductCardDto> results = toolRouter.searchProducts(params);
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should return seeded laptops under 90k");
        for (ProductCardDto card : results) {
            assertTrue(card.getPrice().compareTo(new BigDecimal("90000")) <= 0, "Price must be within budget");
            assertTrue(card.getRating() >= 4.0, "Rating must be at least 4.0");
        }
    }

    @Test
    @DisplayName("Test AI Product Comparison Spec Matrix and Verdict")
    void testProductComparison() {
        List<ProductCardDto> topLaptops = productService.searchProducts("Lenovo", null, null, null, null, 2);
        assertTrue(topLaptops.size() >= 2, "Must have at least 2 laptops for comparison");

        List<Long> ids = topLaptops.stream().map(ProductCardDto::getId).toList();
        ProductComparisonDto comparison = comparisonService.compareProducts(ids);

        assertNotNull(comparison);
        assertEquals(2, comparison.getProducts().size());
        assertNotNull(comparison.getBestOverallId());
        assertNotNull(comparison.getBestValueId());
        assertNotNull(comparison.getAiSummary());
        assertFalse(comparison.getSpecGroups().isEmpty());
    }

    @Test
    @DisplayName("Test Hybrid Recommendation Scoring and Explainability")
    void testHybridRecommendations() {
        List<ProductCardDto> recommendations = recommendationService.getPersonalizedRecommendations(null, 6);
        assertNotNull(recommendations);
        assertEquals(6, recommendations.size());
        for (ProductCardDto card : recommendations) {
            assertNotNull(card.getWhyRecommended(), "Recommendations must contain explainable reasoning");
        }
    }

    @Test
    @DisplayName("Test AI Chat Multi-turn Orchestration")
    void testChatOrchestration() {
        ChatRequest req1 = ChatRequest.builder()
                .message("I need a gaming laptop under 80000")
                .build();

        ChatResponse res1 = aiOrchestrator.processChat(null, "test-session-123", req1);
        assertNotNull(res1);
        assertNotNull(res1.getConversationId());
        assertFalse(res1.getProducts().isEmpty(), "Should recommend candidate laptops");
        assertNotNull(res1.getReasoningSummary());

        // Multi-turn turn 2
        ChatRequest req2 = ChatRequest.builder()
                .conversationId(res1.getConversationId())
                .message("Compare the top two")
                .build();

        ChatResponse res2 = aiOrchestrator.processChat(null, "test-session-123", req2);
        assertNotNull(res2);
        assertEquals(res1.getConversationId(), res2.getConversationId());
        assertEquals("compare_products", res2.getToolUsed());
    }
}
