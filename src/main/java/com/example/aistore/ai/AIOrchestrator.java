package com.example.aistore.ai;

import com.example.aistore.dto.ChatRequest;
import com.example.aistore.dto.ChatResponse;
import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.entity.*;
import com.example.aistore.repository.AIRecommendationLogRepository;
import com.example.aistore.repository.ChatConversationRepository;
import com.example.aistore.repository.ChatMessageRepository;
import com.example.aistore.repository.UserRepository;
import com.example.aistore.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AIOrchestrator {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AIOrchestrator.class);


    private final NvidiaAIProvider nvidiaProvider;
    private final LocalAIProvider localProvider;
    private final MockAIProvider mockProvider;
    private final ToolRouter toolRouter;
    private final ProductService productService;
    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final AIRecommendationLogRepository logRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    public AIOrchestrator(NvidiaAIProvider nvidiaProvider, LocalAIProvider localProvider, MockAIProvider mockProvider, ToolRouter toolRouter, ProductService productService, ChatConversationRepository conversationRepository, ChatMessageRepository messageRepository, AIRecommendationLogRepository logRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.nvidiaProvider = nvidiaProvider;
        this.localProvider = localProvider;
        this.mockProvider = mockProvider;
        this.toolRouter = toolRouter;
        this.productService = productService;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @Value("${ai.provider:mock}")
    private String configuredProvider;

    public AIProvider getActiveProvider() {
        if ("nvidia".equalsIgnoreCase(configuredProvider) && nvidiaProvider.isAvailable()) {
            return nvidiaProvider;
        } else if ("local".equalsIgnoreCase(configuredProvider) && localProvider.isAvailable()) {
            return localProvider;
        }
        return mockProvider;
    }

    @Transactional
    public ChatResponse processChat(Long userId, String sessionId, ChatRequest request) {
        long startTime = System.currentTimeMillis();
        AIProvider provider = getActiveProvider();

        // 1. Get or create conversation entity
        String convId = request.getConversationId();
        if (convId == null || convId.trim().isEmpty()) {
            convId = UUID.randomUUID().toString();
        }

        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;

        String finalConvId = convId;
        ChatConversation conversation = conversationRepository.findByConversationId(convId)
                .orElseGet(() -> {
                    ChatConversation c = ChatConversation.builder()
                            .conversationId(finalConvId)
                            .user(user)
                            .sessionId(sessionId)
                            .title(request.getMessage() != null && request.getMessage().length() > 30 
                                    ? request.getMessage().substring(0, 30) + "..." 
                                    : request.getMessage())
                            .build();
                    return conversationRepository.save(c);
                });

        // 2. Load conversation history for context memory
        List<ChatMessage> previousMessages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
        List<Map<String, String>> historyForPrompt = new ArrayList<>();
        List<Long> previouslyReferencedProductIds = new ArrayList<>();

        for (ChatMessage m : previousMessages) {
            historyForPrompt.add(Map.of(
                    "role", "USER".equalsIgnoreCase(m.getSender()) ? "user" : "assistant",
                    "content", m.getContent()
            ));
            if (m.getRecommendedProductIdsJson() != null) {
                try {
                    List<Long> ids = objectMapper.readValue(m.getRecommendedProductIdsJson(), new TypeReference<List<Long>>() {});
                    previouslyReferencedProductIds.addAll(ids);
                } catch (Exception ignored) {}
            }
        }

        String userMsg = request.getMessage();
        String userMsgLower = userMsg.toLowerCase();

        // Save incoming user message
        ChatMessage userChatMsg = ChatMessage.builder()
                .conversation(conversation)
                .sender("USER")
                .content(userMsg)
                .build();
        messageRepository.save(userChatMsg);

        // 3. Intent Detection & Tool Execution
        String toolUsed = "search_products";
        List<ProductCardDto> products = new ArrayList<>();
        String reasoning = "";
        List<String> followUps = new ArrayList<>();

        if (userMsgLower.contains("compare") || userMsgLower.contains("difference between") || userMsgLower.contains("top two")) {
            toolUsed = "compare_products";
            List<Long> compareIds = extractIdsFromMessage(userMsg);
            if (compareIds.isEmpty() && previouslyReferencedProductIds.size() >= 2) {
                // Multi-turn context memory: compare previously recommended products
                compareIds = previouslyReferencedProductIds.stream().distinct().limit(3).toList();
            }

            if (!compareIds.isEmpty()) {
                ProductComparisonDto compResult = toolRouter.compareProducts(compareIds);
                products = compResult.getProducts();
                reasoning = compResult.getAiSummary();
                followUps.add("Which one has the better battery life?");
                followUps.add("Is the top model worth the extra price?");
            } else {
                products = toolRouter.getRecommendedProducts(userId, 3);
                reasoning = "Please specify which products you'd like to compare, or choose from our top recommendations below:";
            }
        } else if (userMsgLower.contains("feedback") || userMsgLower.contains("complaint") || userMsgLower.contains("review") || userMsgLower.contains("battery") || userMsgLower.contains("camera quality") || userMsgLower.contains("heating") || userMsgLower.contains("customer say")) {
            toolUsed = "get_product_feedback";
            Long prodId = request.getCurrentProductId();
            if (prodId == null && !previouslyReferencedProductIds.isEmpty()) {
                prodId = previouslyReferencedProductIds.get(0);
            }
            if (prodId != null) {
                Map<String, Object> fb = toolRouter.getProductFeedbackSummary(prodId);
                products = productService.getProductsByIds(List.of(prodId));
                String pName = (String) fb.get("productName");
                Object rating = fb.get("rating");
                Object posCount = fb.get("positiveReviews");
                Object negCount = fb.get("negativeReviews");
                Object issues = fb.get("topIssues");
                
                reasoning = String.format("Verified Customer Review Intelligence for %s (Rating: %s★ | %s positive reviews, %s issues noted). Observed topics: %s",
                        pName, rating, posCount, negCount, issues);
            } else {
                reasoning = "Here are our highest-rated products with top customer satisfaction and verified reviews:";
                products = toolRouter.getRecommendedProducts(userId, 3);
            }
        } else {
            // Natural language search tool & database query
            Map<String, Object> searchParams = mockProvider.parseNaturalLanguageSearch(userMsg);
            
            boolean isPureGenericRecommendation = (userMsgLower.contains("recommend") || userMsgLower.contains("suggest") || userMsgLower.contains("for me"))
                    && searchParams.get("category") == null
                    && searchParams.get("brand") == null
                    && searchParams.get("query") == null
                    && searchParams.get("maxPrice") == null
                    && searchParams.get("features") == null;

            if (isPureGenericRecommendation) {
                toolUsed = "get_recommended_products";
                products = toolRouter.getRecommendedProducts(userId, 4);
                reasoning = "Based on your browsing history and preference profile, I selected these top matched products:";
                followUps.add("Compare the top 2 models");
                followUps.add("Show me options under ₹40,000");
            } else {
                toolUsed = "search_products";
                products = toolRouter.searchProducts(searchParams);
                
                if (products.isEmpty()) {
                    // Try relaxing specific constraints while preserving the category
                    String catName = (String) searchParams.get("category");
                    if (catName != null) {
                        Map<String, Object> relaxedParams = new HashMap<>(searchParams);
                        relaxedParams.remove("minRating");
                        relaxedParams.remove("features");
                        relaxedParams.remove("query");
                        products = toolRouter.searchProducts(relaxedParams);
                    }
                }

                if (products.isEmpty()) {
                    products = toolRouter.getRecommendedProducts(userId, 4);
                    reasoning = "I couldn't find exact matches for all specific constraints, so here are the closest top-rated alternatives from our verified catalog:";
                } else {
                    String cat = (String) searchParams.get("category");
                    reasoning = String.format("Found %d verified matching %s products in our store inventory matching your query.", 
                            products.size(), cat != null ? cat : "catalog");
                }

                followUps.add("Compare the top models");
                followUps.add("What do customer reviews say about the battery?");
                followUps.add("Show more details on the top rated option");
            }
        }

        // 4. Generate AI Assistant Message with injected factual database context
        StringBuilder contextBuilder = new StringBuilder();
        if (reasoning != null && !reasoning.isEmpty()) {
            contextBuilder.append("\n[Verified Store Intelligence / Review Facts]: ").append(reasoning).append("\n");
        }
        if (!products.isEmpty()) {
            contextBuilder.append("[Candidate/Active Products from Database]:\n");
            for (ProductCardDto p : products) {
                contextBuilder.append("- ").append(p.getName())
                        .append(" | Price: ₹").append(p.getPrice())
                        .append(" | Rating: ").append(p.getRating()).append("★")
                        .append(" | Specs: ").append(p.getSpecsSummary())
                        .append("\n");
            }
        }

        String enrichedUserMsg = userMsg + (contextBuilder.length() > 0 ? "\n\n" + contextBuilder.toString() : "");

        String aiMessage = provider.generateChatResponse(
                "You are OmniMart's expert AI Shopping Assistant. Use the provided [Verified Store Intelligence / Review Facts] and [Candidate/Active Products from Database] to answer the user accurately, factually, and concisely. Never invent products not in the database.",
                enrichedUserMsg,
                historyForPrompt
        );

        // 5. Persist Assistant Response
        List<Long> recIds = products.stream().map(ProductCardDto::getId).toList();
        String recIdsJson = "[]";
        try {
            recIdsJson = objectMapper.writeValueAsString(recIds);
        } catch (Exception ignored) {}

        ChatMessage assistantMsg = ChatMessage.builder()
                .conversation(conversation)
                .sender("ASSISTANT")
                .content(aiMessage)
                .toolCallsJson(toolUsed)
                .recommendedProductIdsJson(recIdsJson)
                .reasoningSummary(reasoning)
                .build();
        messageRepository.save(assistantMsg);

        // 6. Log AI Recommendation for Guardrails & Audit
        long executionTime = System.currentTimeMillis() - startTime;
        try {
            AIRecommendationLog logEntry = AIRecommendationLog.builder()
                    .user(user)
                    .queryText(userMsg)
                    .toolUsed(toolUsed)
                    .productIdsJson(recIdsJson)
                    .generatedReasoning(reasoning)
                    .providerUsed(provider.getProviderName())
                    .executionTimeMs(executionTime)
                    .build();
            logRepository.save(logEntry);
        } catch (Exception ignored) {}

        return ChatResponse.builder()
                .conversationId(convId)
                .message(aiMessage)
                .products(products)
                .reasoningSummary(reasoning)
                .suggestedFollowUps(followUps)
                .toolUsed(toolUsed)
                .provider(provider.getProviderName())
                .build();
    }

    private List<Long> extractIdsFromMessage(String msg) {
        List<Long> ids = new ArrayList<>();
        Pattern p = Pattern.compile("#?(\\d+)");
        Matcher m = p.matcher(msg);
        while (m.find()) {
            try {
                ids.add(Long.parseLong(m.group(1)));
            } catch (Exception ignored) {}
        }
        return ids;
    }
}
