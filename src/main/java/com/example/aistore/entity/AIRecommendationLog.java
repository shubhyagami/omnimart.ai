package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_recommendation_logs")
public class AIRecommendationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String queryText;

    private String toolUsed;

    @Column(columnDefinition = "TEXT")
    private String productIdsJson;

    @Column(columnDefinition = "TEXT")
    private String generatedReasoning;

    private String providerUsed;
    private long executionTimeMs;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public AIRecommendationLog() {}

    public AIRecommendationLog(Long id, User user, String queryText, String toolUsed, String productIdsJson, String generatedReasoning, String providerUsed, long executionTimeMs, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.queryText = queryText;
        this.toolUsed = toolUsed;
        this.productIdsJson = productIdsJson;
        this.generatedReasoning = generatedReasoning;
        this.providerUsed = providerUsed;
        this.executionTimeMs = executionTimeMs;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String queryText;
        private String toolUsed;
        private String productIdsJson;
        private String generatedReasoning;
        private String providerUsed;
        private long executionTimeMs;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder queryText(String queryText) { this.queryText = queryText; return this; }
        public Builder toolUsed(String toolUsed) { this.toolUsed = toolUsed; return this; }
        public Builder productIdsJson(String productIdsJson) { this.productIdsJson = productIdsJson; return this; }
        public Builder generatedReasoning(String generatedReasoning) { this.generatedReasoning = generatedReasoning; return this; }
        public Builder providerUsed(String providerUsed) { this.providerUsed = providerUsed; return this; }
        public Builder executionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AIRecommendationLog build() {
            return new AIRecommendationLog(id, user, queryText, toolUsed, productIdsJson, generatedReasoning, providerUsed, executionTimeMs, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }

    public String getToolUsed() { return toolUsed; }
    public void setToolUsed(String toolUsed) { this.toolUsed = toolUsed; }

    public String getProductIdsJson() { return productIdsJson; }
    public void setProductIdsJson(String productIdsJson) { this.productIdsJson = productIdsJson; }

    public String getGeneratedReasoning() { return generatedReasoning; }
    public void setGeneratedReasoning(String generatedReasoning) { this.generatedReasoning = generatedReasoning; }

    public String getProviderUsed() { return providerUsed; }
    public void setProviderUsed(String providerUsed) { this.providerUsed = providerUsed; }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
