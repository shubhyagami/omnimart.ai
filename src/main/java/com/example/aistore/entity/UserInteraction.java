package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_interactions")
public class UserInteraction {

    public enum InteractionType {
        PRODUCT_VIEW,
        PRODUCT_CLICK,
        PRODUCT_SEARCH,
        CATEGORY_VIEW,
        CART_ADD,
        CART_REMOVE,
        WISHLIST_ADD,
        PRODUCT_COMPARE,
        PRODUCT_PURCHASE,
        PRODUCT_REVIEW,
        DWELL_TIME
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String categoryName;
    private String brandName;
    private BigDecimal priceAtEvent;
    private int dwellTimeSeconds;

    @Column(columnDefinition = "TEXT")
    private String metadataJson;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public UserInteraction() {}

    public UserInteraction(Long id, User user, String sessionId, InteractionType eventType, Product product, String categoryName, String brandName, BigDecimal priceAtEvent, int dwellTimeSeconds, String metadataJson, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.product = product;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.priceAtEvent = priceAtEvent;
        this.dwellTimeSeconds = dwellTimeSeconds;
        this.metadataJson = metadataJson;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String sessionId;
        private InteractionType eventType;
        private Product product;
        private String categoryName;
        private String brandName;
        private BigDecimal priceAtEvent;
        private int dwellTimeSeconds;
        private String metadataJson;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder eventType(InteractionType eventType) { this.eventType = eventType; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public Builder brandName(String brandName) { this.brandName = brandName; return this; }
        public Builder priceAtEvent(BigDecimal priceAtEvent) { this.priceAtEvent = priceAtEvent; return this; }
        public Builder dwellTimeSeconds(int dwellTimeSeconds) { this.dwellTimeSeconds = dwellTimeSeconds; return this; }
        public Builder metadataJson(String metadataJson) { this.metadataJson = metadataJson; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserInteraction build() {
            return new UserInteraction(id, user, sessionId, eventType, product, categoryName, brandName, priceAtEvent, dwellTimeSeconds, metadataJson, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public InteractionType getEventType() { return eventType; }
    public void setEventType(InteractionType eventType) { this.eventType = eventType; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public BigDecimal getPriceAtEvent() { return priceAtEvent; }
    public void setPriceAtEvent(BigDecimal priceAtEvent) { this.priceAtEvent = priceAtEvent; }

    public int getDwellTimeSeconds() { return dwellTimeSeconds; }
    public void setDwellTimeSeconds(int dwellTimeSeconds) { this.dwellTimeSeconds = dwellTimeSeconds; }

    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String metadataJson) { this.metadataJson = metadataJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
