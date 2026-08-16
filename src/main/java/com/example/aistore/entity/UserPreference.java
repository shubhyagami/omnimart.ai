package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String preferredCategoriesJson;

    @Column(columnDefinition = "TEXT")
    private String preferredBrandsJson;

    private BigDecimal minBudget;
    private BigDecimal maxBudget;

    @Column(columnDefinition = "TEXT")
    private String importantFeaturesJson;

    private boolean recommendationsEnabled = true;
    private boolean behaviorTrackingEnabled = true;
    private boolean aiChatHistoryEnabled = true;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserPreference() {}

    public UserPreference(Long id, User user, String preferredCategoriesJson, String preferredBrandsJson, BigDecimal minBudget, BigDecimal maxBudget, String importantFeaturesJson, boolean recommendationsEnabled, boolean behaviorTrackingEnabled, boolean aiChatHistoryEnabled, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.preferredCategoriesJson = preferredCategoriesJson;
        this.preferredBrandsJson = preferredBrandsJson;
        this.minBudget = minBudget;
        this.maxBudget = maxBudget;
        this.importantFeaturesJson = importantFeaturesJson;
        this.recommendationsEnabled = recommendationsEnabled;
        this.behaviorTrackingEnabled = behaviorTrackingEnabled;
        this.aiChatHistoryEnabled = aiChatHistoryEnabled;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String preferredCategoriesJson;
        private String preferredBrandsJson;
        private BigDecimal minBudget;
        private BigDecimal maxBudget;
        private String importantFeaturesJson;
        private boolean recommendationsEnabled = true;
        private boolean behaviorTrackingEnabled = true;
        private boolean aiChatHistoryEnabled = true;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder preferredCategoriesJson(String preferredCategoriesJson) { this.preferredCategoriesJson = preferredCategoriesJson; return this; }
        public Builder preferredBrandsJson(String preferredBrandsJson) { this.preferredBrandsJson = preferredBrandsJson; return this; }
        public Builder minBudget(BigDecimal minBudget) { this.minBudget = minBudget; return this; }
        public Builder maxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; return this; }
        public Builder importantFeaturesJson(String importantFeaturesJson) { this.importantFeaturesJson = importantFeaturesJson; return this; }
        public Builder recommendationsEnabled(boolean recommendationsEnabled) { this.recommendationsEnabled = recommendationsEnabled; return this; }
        public Builder behaviorTrackingEnabled(boolean behaviorTrackingEnabled) { this.behaviorTrackingEnabled = behaviorTrackingEnabled; return this; }
        public Builder aiChatHistoryEnabled(boolean aiChatHistoryEnabled) { this.aiChatHistoryEnabled = aiChatHistoryEnabled; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public UserPreference build() {
            return new UserPreference(id, user, preferredCategoriesJson, preferredBrandsJson, minBudget, maxBudget, importantFeaturesJson, recommendationsEnabled, behaviorTrackingEnabled, aiChatHistoryEnabled, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPreferredCategoriesJson() { return preferredCategoriesJson; }
    public void setPreferredCategoriesJson(String preferredCategoriesJson) { this.preferredCategoriesJson = preferredCategoriesJson; }

    public String getPreferredBrandsJson() { return preferredBrandsJson; }
    public void setPreferredBrandsJson(String preferredBrandsJson) { this.preferredBrandsJson = preferredBrandsJson; }

    public BigDecimal getMinBudget() { return minBudget; }
    public void setMinBudget(BigDecimal minBudget) { this.minBudget = minBudget; }

    public BigDecimal getMaxBudget() { return maxBudget; }
    public void setMaxBudget(BigDecimal maxBudget) { this.maxBudget = maxBudget; }

    public String getImportantFeaturesJson() { return importantFeaturesJson; }
    public void setImportantFeaturesJson(String importantFeaturesJson) { this.importantFeaturesJson = importantFeaturesJson; }

    public boolean isRecommendationsEnabled() { return recommendationsEnabled; }
    public void setRecommendationsEnabled(boolean recommendationsEnabled) { this.recommendationsEnabled = recommendationsEnabled; }

    public boolean isBehaviorTrackingEnabled() { return behaviorTrackingEnabled; }
    public void setBehaviorTrackingEnabled(boolean behaviorTrackingEnabled) { this.behaviorTrackingEnabled = behaviorTrackingEnabled; }

    public boolean isAiChatHistoryEnabled() { return aiChatHistoryEnabled; }
    public void setAiChatHistoryEnabled(boolean aiChatHistoryEnabled) { this.aiChatHistoryEnabled = aiChatHistoryEnabled; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
