package com.example.aistore.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class UserPreferenceDto {
    private Map<String, Integer> preferredCategories;
    private Map<String, Integer> preferredBrands;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private List<String> importantFeatures;
    private boolean recommendationsEnabled;
    private boolean behaviorTrackingEnabled;
    private boolean aiChatHistoryEnabled;

    public UserPreferenceDto() {}

    public UserPreferenceDto(Map<String, Integer> preferredCategories, Map<String, Integer> preferredBrands, BigDecimal minBudget, BigDecimal maxBudget, List<String> importantFeatures, boolean recommendationsEnabled, boolean behaviorTrackingEnabled, boolean aiChatHistoryEnabled) {
        this.preferredCategories = preferredCategories;
        this.preferredBrands = preferredBrands;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.importantFeatures = importantFeatures;
        this.recommendationsEnabled = recommendationsEnabled;
        this.behaviorTrackingEnabled = behaviorTrackingEnabled;
        this.aiChatHistoryEnabled = aiChatHistoryEnabled;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Map<String, Integer> preferredCategories;
        private Map<String, Integer> preferredBrands;
        private BigDecimal minBudget;
        private BigDecimal maxBudget;
        private List<String> importantFeatures;
        private boolean recommendationsEnabled = true;
        private boolean behaviorTrackingEnabled = true;
        private boolean aiChatHistoryEnabled = true;

        public Builder preferredCategories(Map<String, Integer> preferredCategories) { this.preferredCategories = preferredCategories; return this; }
        public Builder preferredBrands(Map<String, Integer> preferredBrands) { this.preferredBrands = preferredBrands; return this; }
        public Builder minBudget(BigDecimal minBudget) { this.minBudget = minBudget; return this; }
        public Builder maxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; return this; }
        public Builder importantFeatures(List<String> importantFeatures) { this.importantFeatures = importantFeatures; return this; }
        public Builder recommendationsEnabled(boolean recommendationsEnabled) { this.recommendationsEnabled = recommendationsEnabled; return this; }
        public Builder behaviorTrackingEnabled(boolean behaviorTrackingEnabled) { this.behaviorTrackingEnabled = behaviorTrackingEnabled; return this; }
        public Builder aiChatHistoryEnabled(boolean aiChatHistoryEnabled) { this.aiChatHistoryEnabled = aiChatHistoryEnabled; return this; }

        public UserPreferenceDto build() {
            return new UserPreferenceDto(preferredCategories, preferredBrands, minBudget, maxBudget, importantFeatures, recommendationsEnabled, behaviorTrackingEnabled, aiChatHistoryEnabled);
        }
    }

    public Map<String, Integer> getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(Map<String, Integer> preferredCategories) { this.preferredCategories = preferredCategories; }

    public Map<String, Integer> getPreferredBrands() { return preferredBrands; }
    public void setPreferredBrands(Map<String, Integer> preferredBrands) { this.preferredBrands = preferredBrands; }

    public BigDecimal getMinBudget() { return minBudget; }
    public void setMinBudget(BigDecimal minBudget) { this.minBudget = minBudget; }

    public BigDecimal getMaxBudget() { return maxBudget; }
    public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }

    public List<String> getImportantFeatures() { return importantFeatures; }
    public void setImportantFeatures(List<String> importantFeatures) { this.importantFeatures = importantFeatures; }

    public boolean isRecommendationsEnabled() { return recommendationsEnabled; }
    public void setRecommendationsEnabled(boolean recommendationsEnabled) { this.recommendationsEnabled = recommendationsEnabled; }

    public boolean isBehaviorTrackingEnabled() { return behaviorTrackingEnabled; }
    public void setBehaviorTrackingEnabled(boolean behaviorTrackingEnabled) { this.behaviorTrackingEnabled = behaviorTrackingEnabled; }

    public boolean isAiChatHistoryEnabled() { return aiChatHistoryEnabled; }
    public void setAiChatHistoryEnabled(boolean aiChatHistoryEnabled) { this.aiChatHistoryEnabled = aiChatHistoryEnabled; }
}
