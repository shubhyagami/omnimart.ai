package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_feedbacks")
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String sentiment;
    private String emotion;
    private String primaryTopic;

    @Column(columnDefinition = "TEXT")
    private String specificIssuesJson;

    @Column(columnDefinition = "TEXT")
    private String positiveAspectsJson;

    private double confidenceScore;
    private String source;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public CustomerFeedback() {}

    public CustomerFeedback(Long id, Review review, Product product, User user, String sentiment, String emotion, String primaryTopic, String specificIssuesJson, String positiveAspectsJson, double confidenceScore, String source, LocalDateTime createdAt) {
        this.id = id;
        this.review = review;
        this.product = product;
        this.user = user;
        this.sentiment = sentiment;
        this.emotion = emotion;
        this.primaryTopic = primaryTopic;
        this.specificIssuesJson = specificIssuesJson;
        this.positiveAspectsJson = positiveAspectsJson;
        this.confidenceScore = confidenceScore;
        this.source = source;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Review review;
        private Product product;
        private User user;
        private String sentiment;
        private String emotion;
        private String primaryTopic;
        private String specificIssuesJson;
        private String positiveAspectsJson;
        private double confidenceScore;
        private String source;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder review(Review review) { this.review = review; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder sentiment(String sentiment) { this.sentiment = sentiment; return this; }
        public Builder emotion(String emotion) { this.emotion = emotion; return this; }
        public Builder primaryTopic(String primaryTopic) { this.primaryTopic = primaryTopic; return this; }
        public Builder specificIssuesJson(String specificIssuesJson) { this.specificIssuesJson = specificIssuesJson; return this; }
        public Builder positiveAspectsJson(String positiveAspectsJson) { this.positiveAspectsJson = positiveAspectsJson; return this; }
        public Builder confidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CustomerFeedback build() {
            return new CustomerFeedback(id, review, product, user, sentiment, emotion, primaryTopic, specificIssuesJson, positiveAspectsJson, confidenceScore, source, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Review getReview() { return review; }
    public void setReview(Review review) { this.review = review; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public String getPrimaryTopic() { return primaryTopic; }
    public void setPrimaryTopic(String primaryTopic) { this.primaryTopic = primaryTopic; }

    public String getSpecificIssuesJson() { return specificIssuesJson; }
    public void setSpecificIssuesJson(String specificIssuesJson) { this.specificIssuesJson = specificIssuesJson; }

    public String getPositiveAspectsJson() { return positiveAspectsJson; }
    public void setPositiveAspectsJson(String positiveAspectsJson) { this.positiveAspectsJson = positiveAspectsJson; }

    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
