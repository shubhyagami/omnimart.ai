package com.example.aistore.service;

import com.example.aistore.ai.MockAIProvider;
import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.Category;
import com.example.aistore.entity.SearchHistory;
import com.example.aistore.entity.User;
import com.example.aistore.repository.CategoryRepository;
import com.example.aistore.repository.SearchHistoryRepository;
import com.example.aistore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SearchIntelligenceService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SearchIntelligenceService.class);


    private final MockAIProvider mockAIProvider;
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    public SearchIntelligenceService(MockAIProvider mockAIProvider, ProductService productService, CategoryRepository categoryRepository, SearchHistoryRepository searchHistoryRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.mockAIProvider = mockAIProvider;
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public List<ProductCardDto> processNaturalLanguageSearch(Long userId, String sessionId, String query) {
        Map<String, Object> extracted = mockAIProvider.parseNaturalLanguageSearch(query);

        String catName = (String) extracted.get("category");
        Long categoryId = null;
        if (catName != null) {
            categoryId = categoryRepository.findByNameIgnoreCase(catName)
                    .map(Category::getId)
                    .orElse(null);
        }

        BigDecimal minPrice = (BigDecimal) extracted.get("minPrice");
        BigDecimal maxPrice = (BigDecimal) extracted.get("maxPrice");
        Double minRating = (Double) extracted.get("minRating");
        String cleanQuery = (String) extracted.get("query");

        List<ProductCardDto> results = productService.searchProducts(
                cleanQuery != null ? cleanQuery : query,
                categoryId,
                minPrice,
                maxPrice,
                minRating,
                20
        );

        // Record search history
        try {
            User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
            SearchHistory history = SearchHistory.builder()
                    .user(user)
                    .sessionId(sessionId)
                    .searchQuery(query)
                    .filtersAppliedJson(objectMapper.writeValueAsString(extracted))
                    .resultCount(results.size())
                    .build();
            searchHistoryRepository.save(history);
        } catch (Exception ignored) {}

        return results;
    }
}
