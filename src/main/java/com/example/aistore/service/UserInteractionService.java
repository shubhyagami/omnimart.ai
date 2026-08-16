package com.example.aistore.service;

import com.example.aistore.entity.Product;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import com.example.aistore.entity.UserPreference;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.UserInteractionRepository;
import com.example.aistore.repository.UserPreferenceRepository;
import com.example.aistore.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserInteractionService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserInteractionService.class);


    private final UserInteractionRepository interactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserPreferenceService preferenceService;
    public UserInteractionService(UserInteractionRepository interactionRepository, ProductRepository productRepository, UserRepository userRepository, UserPreferenceService preferenceService) {
        this.interactionRepository = interactionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.preferenceService = preferenceService;
    }


    @Transactional
    public void recordInteraction(Long userId, String sessionId, UserInteraction.InteractionType eventType, Long productId, int dwellTimeSeconds, String metadataJson) {
        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
        
        // Privacy Guardrail: Check if behavior tracking is disabled by the user
        if (user != null && user.getUserPreference() != null && !user.getUserPreference().isBehaviorTrackingEnabled()) {
            return;
        }

        Product product = productId != null ? productRepository.findById(productId).orElse(null) : null;

        UserInteraction.Builder builder = UserInteraction.builder()
                .user(user)
                .sessionId(sessionId)
                .eventType(eventType)
                .product(product)
                .dwellTimeSeconds(dwellTimeSeconds)
                .metadataJson(metadataJson);

        if (product != null) {
            builder.categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                   .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                   .priceAtEvent(product.getPrice());
        }

        interactionRepository.save(builder.build());

        // Asynchronously or trigger dynamic recalculation of user preferences
        if (user != null) {
            preferenceService.updateUserPreferencesFromBehavior(user.getId());
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getRecentlyViewedProductIds(Long userId, int limit) {
        if (userId == null) return Collections.emptyList();
        return interactionRepository.findRecentlyInteractedProductIds(userId, PageRequest.of(0, limit));
    }
}
