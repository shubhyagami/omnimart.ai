package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_comparisons")
public class ProductComparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String sessionId;

    @Column(nullable = false)
    private String comparedProductIdsJson;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String aiSummary;

    private Long bestOverallProductId;
    private Long bestValueProductId;
    private Long bestCameraProductId;
    private Long bestPerformanceProductId;
    private Long bestBatteryProductId;

    @Column(columnDefinition = "TEXT")
    private String categoryBreakdownJson;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ProductComparison() {}

    public ProductComparison(Long id, User user, String sessionId, String comparedProductIdsJson, String aiSummary, Long bestOverallProductId, Long bestValueProductId, Long bestCameraProductId, Long bestPerformanceProductId, Long bestBatteryProductId, String categoryBreakdownJson, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.comparedProductIdsJson = comparedProductIdsJson;
        this.aiSummary = aiSummary;
        this.bestOverallProductId = bestOverallProductId;
        this.bestValueProductId = bestValueProductId;
        this.bestCameraProductId = bestCameraProductId;
        this.bestPerformanceProductId = bestPerformanceProductId;
        this.bestBatteryProductId = bestBatteryProductId;
        this.categoryBreakdownJson = categoryBreakdownJson;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String sessionId;
        private String comparedProductIdsJson;
        private String aiSummary;
        private Long bestOverallProductId;
        private Long bestValueProductId;
        private Long bestCameraProductId;
        private Long bestPerformanceProductId;
        private Long bestBatteryProductId;
        private String categoryBreakdownJson;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder comparedProductIdsJson(String comparedProductIdsJson) { this.comparedProductIdsJson = comparedProductIdsJson; return this; }
        public Builder aiSummary(String aiSummary) { this.aiSummary = aiSummary; return this; }
        public Builder bestOverallProductId(Long bestOverallProductId) { this.bestOverallProductId = bestOverallProductId; return this; }
        public Builder bestValueProductId(Long bestValueProductId) { this.bestValueProductId = bestValueProductId; return this; }
        public Builder bestCameraProductId(Long bestCameraProductId) { this.bestCameraProductId = bestCameraProductId; return this; }
        public Builder bestPerformanceProductId(Long bestPerformanceProductId) { this.bestPerformanceProductId = bestPerformanceProductId; return this; }
        public Builder bestBatteryProductId(Long bestBatteryProductId) { this.bestBatteryProductId = bestBatteryProductId; return this; }
        public Builder categoryBreakdownJson(String categoryBreakdownJson) { this.categoryBreakdownJson = categoryBreakdownJson; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ProductComparison build() {
            return new ProductComparison(id, user, sessionId, comparedProductIdsJson, aiSummary, bestOverallProductId, bestValueProductId, bestCameraProductId, bestPerformanceProductId, bestBatteryProductId, categoryBreakdownJson, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getComparedProductIdsJson() { return comparedProductIdsJson; }
    public void setComparedProductIdsJson(String comparedProductIdsJson) { this.comparedProductIdsJson = comparedProductIdsJson; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public Long getBestOverallProductId() { return bestOverallProductId; }
    public void setBestOverallProductId(Long bestOverallProductId) { this.bestOverallProductId = bestOverallProductId; }

    public Long getBestValueProductId() { return bestValueProductId; }
    public void setBestValueProductId(Long bestValueProductId) { this.bestValueProductId = bestValueProductId; }

    public Long getBestCameraProductId() { return bestCameraProductId; }
    public void setBestCameraProductId(Long bestCameraProductId) { this.bestCameraProductId = bestCameraProductId; }

    public Long getBestPerformanceProductId() { return bestPerformanceProductId; }
    public void setBestPerformanceProductId(Long bestPerformanceProductId) { this.bestPerformanceProductId = bestPerformanceProductId; }

    public Long getBestBatteryProductId() { return bestBatteryProductId; }
    public void setBestBatteryProductId(Long bestBatteryProductId) { this.bestBatteryProductId = bestBatteryProductId; }

    public String getCategoryBreakdownJson() { return categoryBreakdownJson; }
    public void setCategoryBreakdownJson(String categoryBreakdownJson) { this.categoryBreakdownJson = categoryBreakdownJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
