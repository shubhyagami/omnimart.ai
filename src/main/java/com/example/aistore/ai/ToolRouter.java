package com.example.aistore.ai;

import com.example.aistore.dto.FeedbackAnalysisDto;
import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.dto.UserPreferenceDto;
import com.example.aistore.entity.CustomerFeedback;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.Review;
import com.example.aistore.repository.CategoryRepository;
import com.example.aistore.repository.CustomerFeedbackRepository;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.ReviewRepository;
import com.example.aistore.service.HybridRecommendationService;
import com.example.aistore.service.ProductComparisonService;
import com.example.aistore.service.ProductService;
import com.example.aistore.service.UserPreferenceService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ToolRouter {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ToolRouter.class);


    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final com.example.aistore.repository.BrandRepository brandRepository;
    private final HybridRecommendationService recommendationService;
    private final ProductComparisonService comparisonService;
    private final UserPreferenceService userPreferenceService;
    private final ReviewRepository reviewRepository;
    private final CustomerFeedbackRepository feedbackRepository;
    public ToolRouter(ProductService productService, ProductRepository productRepository, CategoryRepository categoryRepository, com.example.aistore.repository.BrandRepository brandRepository, HybridRecommendationService recommendationService, ProductComparisonService comparisonService, UserPreferenceService userPreferenceService, ReviewRepository reviewRepository, CustomerFeedbackRepository feedbackRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.recommendationService = recommendationService;
        this.comparisonService = comparisonService;
        this.userPreferenceService = userPreferenceService;
        this.reviewRepository = reviewRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> searchProducts(Map<String, Object> params) {
        String query = (String) params.get("query");
        String categoryName = (String) params.get("category");
        Long categoryId = null;
        if (categoryName != null) {
            categoryId = categoryRepository.findByNameIgnoreCase(categoryName)
                    .map(c -> c.getId())
                    .orElse(null);
        }

        String brandName = (String) params.get("brand");
        Long brandId = null;
        if (brandName != null) {
            brandId = brandRepository.findByNameIgnoreCase(brandName)
                    .map(b -> b.getId())
                    .orElse(null);
        }

        BigDecimal minPrice = null;
        if (params.get("minPrice") != null) {
            minPrice = new BigDecimal(params.get("minPrice").toString());
        }

        BigDecimal maxPrice = null;
        if (params.get("maxPrice") != null) {
            maxPrice = new BigDecimal(params.get("maxPrice").toString());
        }

        Double minRating = null;
        if (params.get("minRating") != null) {
            minRating = Double.valueOf(params.get("minRating").toString());
        }

        @SuppressWarnings("unchecked")
        List<String> features = (List<String>) params.get("features");

        int limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 8;

        return productService.searchProducts(query, categoryId, brandId, minPrice, maxPrice, minRating, features, limit);
    }

    @Transactional(readOnly = true)
    public ProductComparisonDto compareProducts(List<Long> productIds) {
        return comparisonService.compareProducts(productIds);
    }

    @Transactional(readOnly = true)
    public UserPreferenceDto getUserPreferences(Long userId) {
        return userPreferenceService.getUserPreferenceDto(userId);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getRecommendedProducts(Long userId, int limit) {
        return recommendationService.getPersonalizedRecommendations(userId, limit);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getProductFeedbackSummary(Long productId) {
        Map<String, Object> res = new HashMap<>();
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return res;

        List<CustomerFeedback> feedbacks = feedbackRepository.findByProduct(product);
        long positiveCount = feedbacks.stream().filter(f -> "Positive".equalsIgnoreCase(f.getSentiment())).count();
        long negativeCount = feedbacks.stream().filter(f -> "Negative".equalsIgnoreCase(f.getSentiment())).count();

        res.put("productId", productId);
        res.put("productName", product.getName());
        res.put("rating", product.getRating());
        res.put("reviewCount", product.getReviewCount());
        res.put("positiveReviews", positiveCount);
        res.put("negativeReviews", negativeCount);

        List<String> topIssues = feedbacks.stream()
                .filter(f -> "Negative".equalsIgnoreCase(f.getSentiment()))
                .map(CustomerFeedback::getPrimaryTopic)
                .filter(Objects::nonNull)
                .distinct()
                .limit(4)
                .toList();

        res.put("topIssues", topIssues);
        return res;
    }

    @Transactional(readOnly = true)
    public boolean checkStock(Long productId, int quantity) {
        return productService.checkStockAvailability(productId, quantity);
    }
}
