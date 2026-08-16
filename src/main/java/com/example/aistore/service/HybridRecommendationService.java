package com.example.aistore.service;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.UserPreferenceDto;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.UserInteractionRepository;
import com.example.aistore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HybridRecommendationService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HybridRecommendationService.class);


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserPreferenceService userPreferenceService;
    private final UserInteractionRepository interactionRepository;
    private final ProductService productService;
    public HybridRecommendationService(ProductRepository productRepository, UserRepository userRepository, UserPreferenceService userPreferenceService, UserInteractionRepository interactionRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userPreferenceService = userPreferenceService;
        this.interactionRepository = interactionRepository;
        this.productService = productService;
    }


    @Value("${ai.recommendation.weights.user-preference:0.35}")
    private double weightUserPref;

    @Value("${ai.recommendation.weights.behavioral-similarity:0.25}")
    private double weightBehavioral;

    @Value("${ai.recommendation.weights.content-relevance:0.20}")
    private double weightContent;

    @Value("${ai.recommendation.weights.rating:0.10}")
    private double weightRating;

    @Value("${ai.recommendation.weights.popularity:0.10}")
    private double weightPopularity;

    @Transactional(readOnly = true)
    public List<ProductCardDto> getPersonalizedRecommendations(Long userId, int limit) {
        List<Product> allProducts = productRepository.findAll().stream()
                .filter(Product::isActive)
                .toList();

        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }

        User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
        UserPreferenceDto pref = userPreferenceService.getUserPreferenceDto(userId);

        // If user disabled recommendations, fall back to top rated/popular
        if (pref != null && !pref.isRecommendationsEnabled()) {
            return productService.getTopRatedProducts().stream().limit(limit).toList();
        }

        // Get recently viewed product categories/brands for behavioral similarity
        Set<String> recentCategories = new HashSet<>();
        Set<String> recentBrands = new HashSet<>();
        if (userId != null && (pref == null || pref.isBehaviorTrackingEnabled())) {
            List<UserInteraction> interactions = interactionRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 20));
            for (UserInteraction ui : interactions) {
                if (ui.getCategoryName() != null) recentCategories.add(ui.getCategoryName());
                if (ui.getBrandName() != null) recentBrands.add(ui.getBrandName());
            }
        }

        // Max review count for normalizing popularity score
        int maxReviews = allProducts.stream().mapToInt(Product::getReviewCount).max().orElse(1);
        if (maxReviews == 0) maxReviews = 1;

        Map<Product, ScoredProduct> scoredMap = new HashMap<>();

        for (Product p : allProducts) {
            // 1. User Preference Score (0.0 to 1.0)
            double userPrefScore = 0.0;
            List<String> reasons = new ArrayList<>();

            String catName = p.getCategory() != null ? p.getCategory().getName() : "";
            String brandName = p.getBrand() != null ? p.getBrand().getName() : "";

            if (pref != null && pref.getPreferredCategories() != null && pref.getPreferredCategories().containsKey(catName)) {
                int catPct = pref.getPreferredCategories().get(catName);
                userPrefScore += (catPct / 100.0) * 0.6;
                reasons.add("Matches preferred category (" + catName + ")");
            }

            if (pref != null && pref.getPreferredBrands() != null && pref.getPreferredBrands().containsKey(brandName)) {
                int brandPct = pref.getPreferredBrands().get(brandName);
                userPrefScore += (brandPct / 100.0) * 0.4;
                reasons.add("Preferred brand (" + brandName + ")");
            }

            // Budget affinity
            if (pref != null && pref.getMinBudget() != null && pref.getMaxBudget() != null) {
                if (p.getPrice().compareTo(pref.getMinBudget()) >= 0 && p.getPrice().compareTo(pref.getMaxBudget()) <= 0) {
                    userPrefScore += 0.2;
                    reasons.add("Within your preferred budget range");
                }
            }

            userPrefScore = Math.min(1.0, userPrefScore);

            // 2. Behavioral Similarity Score (0.0 to 1.0)
            double behavioralScore = 0.0;
            if (recentCategories.contains(catName)) {
                behavioralScore += 0.6;
                reasons.add("Similar to recently browsed " + catName);
            }
            if (recentBrands.contains(brandName)) {
                behavioralScore += 0.4;
            }
            behavioralScore = Math.min(1.0, behavioralScore);

            // 3. Content Relevance Score (0.0 to 1.0)
            double contentScore = 0.5; // Baseline
            if (p.getTags() != null && (p.getTags().toLowerCase().contains("flagship") || p.getTags().toLowerCase().contains("gaming") || p.getTags().toLowerCase().contains("bestseller"))) {
                contentScore = 0.9;
            }

            // 4. Rating Score (0.0 to 1.0)
            double ratingScore = p.getRating() / 5.0;
            if (p.getRating() >= 4.5) {
                reasons.add("Top rated (" + p.getRating() + "★)");
            }

            // 5. Popularity Score (0.0 to 1.0)
            double popularityScore = (double) p.getReviewCount() / maxReviews;

            // Hybrid Weighted Calculation
            double hybridScore = (weightUserPref * userPrefScore)
                               + (weightBehavioral * behavioralScore)
                               + (weightContent * contentScore)
                               + (weightRating * ratingScore)
                               + (weightPopularity * popularityScore);

            String combinedReason = reasons.isEmpty() ? "Popular top choice in " + catName : String.join(" • ", reasons);

            scoredMap.put(p, new ScoredProduct(p, hybridScore, combinedReason));
        }

        return scoredMap.values().stream()
                .sorted(Comparator.comparingDouble(ScoredProduct::score).reversed())
                .limit(limit)
                .map(sp -> {
                    ProductCardDto card = productService.toCardDto(sp.product());
                    card.setWhyRecommended(sp.reason());
                    return card;
                })
                .collect(Collectors.toList());
    }

    private record ScoredProduct(Product product, double score, String reason) {}
}
