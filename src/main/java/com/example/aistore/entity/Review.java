package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;

    private boolean verifiedPurchase = true;
    private String sentiment;
    private int helpfulCount = 0;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CustomerFeedback customerFeedback;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Review() {}

    public Review(Long id, User user, Product product, int rating, String title, String comment, boolean verifiedPurchase, String sentiment, int helpfulCount, CustomerFeedback customerFeedback, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.title = title;
        this.comment = comment;
        this.verifiedPurchase = verifiedPurchase;
        this.sentiment = sentiment;
        this.helpfulCount = helpfulCount;
        this.customerFeedback = customerFeedback;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private Product product;
        private int rating;
        private String title;
        private String comment;
        private boolean verifiedPurchase = true;
        private String sentiment;
        private int helpfulCount = 0;
        private CustomerFeedback customerFeedback;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder rating(int rating) { this.rating = rating; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }
        public Builder verifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; return this; }
        public Builder sentiment(String sentiment) { this.sentiment = sentiment; return this; }
        public Builder helpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; return this; }
        public Builder customerFeedback(CustomerFeedback customerFeedback) { this.customerFeedback = customerFeedback; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Review build() {
            return new Review(id, user, product, rating, title, comment, verifiedPurchase, sentiment, helpfulCount, customerFeedback, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public boolean isVerifiedPurchase() { return verifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { this.verifiedPurchase = verifiedPurchase; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public int getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(int helpfulCount) { this.helpfulCount = helpfulCount; }

    public CustomerFeedback getCustomerFeedback() { return customerFeedback; }
    public void setCustomerFeedback(CustomerFeedback customerFeedback) { this.customerFeedback = customerFeedback; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
