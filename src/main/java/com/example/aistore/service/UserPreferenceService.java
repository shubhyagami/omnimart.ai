package com.example.aistore.service;

import com.example.aistore.dto.UserPreferenceDto;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserPreference;
import com.example.aistore.repository.UserInteractionRepository;
import com.example.aistore.repository.UserPreferenceRepository;
import com.example.aistore.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserPreferenceService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserPreferenceService.class);


    private final UserPreferenceRepository preferenceRepository;
    private final UserInteractionRepository interactionRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    public UserPreferenceService(UserPreferenceRepository preferenceRepository, UserInteractionRepository interactionRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.preferenceRepository = preferenceRepository;
        this.interactionRepository = interactionRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public void updateUserPreferencesFromBehavior(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        UserPreference preference = preferenceRepository.findByUser(user)
                .orElseGet(() -> UserPreference.builder().user(user).build());

        if (!preference.isBehaviorTrackingEnabled()) {
            return;
        }

        // 1. Calculate Category Affinity Percentages
        List<Object[]> catRows = interactionRepository.getUserCategoryAffinity(userId);
        long totalCatEvents = catRows.stream().mapToLong(r -> (Long) r[1]).sum();
        Map<String, Integer> catMap = new LinkedHashMap<>();
        if (totalCatEvents > 0) {
            for (Object[] row : catRows) {
                String cat = (String) row[0];
                long count = (Long) row[1];
                int pct = (int) Math.round(((double) count / totalCatEvents) * 100.0);
                catMap.put(cat, pct);
            }
        }

        // 2. Calculate Brand Affinity Percentages
        List<Object[]> brandRows = interactionRepository.getUserBrandAffinity(userId);
        long totalBrandEvents = brandRows.stream().mapToLong(r -> (Long) r[1]).sum();
        Map<String, Integer> brandMap = new LinkedHashMap<>();
        if (totalBrandEvents > 0) {
            for (Object[] row : brandRows) {
                String brand = (String) row[0];
                long count = (Long) row[1];
                int pct = (int) Math.round(((double) count / totalBrandEvents) * 100.0);
                brandMap.put(brand, pct);
            }
        }

        // 3. Calculate Price Range
        List<Object[]> priceRows = interactionRepository.getUserPriceRangeStats(userId);
        if (!priceRows.isEmpty() && priceRows.get(0)[1] != null) {
            BigDecimal minP = (BigDecimal) priceRows.get(0)[1];
            BigDecimal maxP = (BigDecimal) priceRows.get(0)[2];
            preference.setMinBudget(minP);
            preference.setMaxBudget(maxP);
        }

        try {
            preference.setPreferredCategoriesJson(objectMapper.writeValueAsString(catMap));
            preference.setPreferredBrandsJson(objectMapper.writeValueAsString(brandMap));

            // Default features based on top category
            List<String> features = new ArrayList<>();
            if (catMap.containsKey("Gaming") || catMap.containsKey("Laptops")) {
                features.addAll(List.of("GPU Performance", "High Refresh Rate", "Thermals", "RAM Capacity"));
            } else if (catMap.containsKey("Smartphones")) {
                features.addAll(List.of("Camera OIS", "AMOLED Screen", "Fast Charging", "Battery"));
            } else {
                features.addAll(List.of("Build Quality", "Battery Life", "Value For Money"));
            }
            preference.setImportantFeaturesJson(objectMapper.writeValueAsString(features));

            preferenceRepository.save(preference);
        } catch (Exception e) {
            log.error("Failed to serialize user preference maps: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public UserPreferenceDto getUserPreferenceDto(Long userId) {
        if (userId == null) {
            return getDefaultPreferenceDto();
        }

        UserPreference preference = preferenceRepository.findByUserId(userId).orElse(null);
        if (preference == null) {
            return getDefaultPreferenceDto();
        }

        Map<String, Integer> categories = parseMap(preference.getPreferredCategoriesJson());
        Map<String, Integer> brands = parseMap(preference.getPreferredBrandsJson());
        List<String> features = parseList(preference.getImportantFeaturesJson());

        return UserPreferenceDto.builder()
                .preferredCategories(categories)
                .preferredBrands(brands)
                .minBudget(preference.getMinBudget())
                .maxBudget(preference.getMaxBudget())
                .importantFeatures(features)
                .recommendationsEnabled(preference.isRecommendationsEnabled())
                .behaviorTrackingEnabled(preference.isBehaviorTrackingEnabled())
                .aiChatHistoryEnabled(preference.isAiChatHistoryEnabled())
                .build();
    }

    @Transactional
    public void updatePrivacySettings(Long userId, boolean recommendationsEnabled, boolean behaviorTrackingEnabled, boolean aiChatHistoryEnabled) {
        User user = userRepository.findById(userId).orElseThrow();
        UserPreference preference = preferenceRepository.findByUser(user)
                .orElseGet(() -> UserPreference.builder().user(user).build());

        preference.setRecommendationsEnabled(recommendationsEnabled);
        preference.setBehaviorTrackingEnabled(behaviorTrackingEnabled);
        preference.setAiChatHistoryEnabled(aiChatHistoryEnabled);
        preferenceRepository.save(preference);
    }

    @Transactional
    public void updateExplicitPreferences(Long userId, BigDecimal minBudget, BigDecimal maxBudget, List<String> categories, List<String> brands) {
        User user = userRepository.findById(userId).orElseThrow();
        UserPreference preference = preferenceRepository.findByUser(user)
                .orElseGet(() -> UserPreference.builder().user(user).build());

        if (minBudget != null) preference.setMinBudget(minBudget);
        if (maxBudget != null) preference.setMaxBudget(maxBudget);

        try {
            if (categories != null && !categories.isEmpty()) {
                Map<String, Integer> catMap = new LinkedHashMap<>();
                int weight = 100 / categories.size();
                for (String c : categories) catMap.put(c.trim(), weight);
                preference.setPreferredCategoriesJson(objectMapper.writeValueAsString(catMap));
            }
            if (brands != null && !brands.isEmpty()) {
                Map<String, Integer> brandMap = new LinkedHashMap<>();
                int weight = 100 / brands.size();
                for (String b : brands) brandMap.put(b.trim(), weight);
                preference.setPreferredBrandsJson(objectMapper.writeValueAsString(brandMap));
            }
        } catch (Exception e) {
            log.warn("Error serializing user preference JSON: {}", e.getMessage());
        }

        preferenceRepository.save(preference);
    }

    private Map<String, Integer> parseMap(String json) {
        if (json == null || json.trim().isEmpty()) return Collections.emptyMap();
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Integer>>() {});
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    private List<String> parseList(String json) {
        if (json == null || json.trim().isEmpty()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private UserPreferenceDto getDefaultPreferenceDto() {
        return UserPreferenceDto.builder()
                .preferredCategories(Map.of("Laptops", 50, "Smartphones", 50))
                .preferredBrands(Map.of("Samsung", 40, "Lenovo", 30, "Apple", 30))
                .minBudget(BigDecimal.valueOf(10000))
                .maxBudget(BigDecimal.valueOf(100000))
                .importantFeatures(List.of("Performance", "Battery", "Display"))
                .recommendationsEnabled(true)
                .behaviorTrackingEnabled(true)
                .aiChatHistoryEnabled(true)
                .build();
    }
}
