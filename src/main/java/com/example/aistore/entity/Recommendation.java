package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private double hybridScore;

    @Column(columnDefinition = "TEXT")
    private String reasonDescription;

    private String recommendationType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Recommendation() {}

    public Recommendation(Long id, User user, Product product, double hybridScore, String reasonDescription, String recommendationType, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.hybridScore = hybridScore;
        this.reasonDescription = reasonDescription;
        this.recommendationType = recommendationType;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private Product product;
        private double hybridScore;
        private String reasonDescription;
        private String recommendationType;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder hybridScore(double hybridScore) { this.hybridScore = hybridScore; return this; }
        public Builder reasonDescription(String reasonDescription) { this.reasonDescription = reasonDescription; return this; }
        public Builder recommendationType(String recommendationType) { this.recommendationType = recommendationType; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Recommendation build() {
            return new Recommendation(id, user, product, hybridScore, reasonDescription, recommendationType, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public double getHybridScore() { return hybridScore; }
    public void setHybridScore(double hybridScore) { this.hybridScore = hybridScore; }

    public String getReasonDescription() { return reasonDescription; }
    public void setReasonDescription(String reasonDescription) { this.reasonDescription = reasonDescription; }

    public String getRecommendationType() { return recommendationType; }
    public void setRecommendationType(String recommendationType) { this.recommendationType = recommendationType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
