package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_products")
public class MarketProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String productName;

    private String brand;
    private String category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal externalPrice;

    private String externalUrl;
    private LocalDate observedDate;
    private double benchmarkRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_product_id")
    private Product matchedProduct;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public MarketProduct() {}

    public MarketProduct(Long id, String storeName, String productName, String brand, String category, BigDecimal externalPrice, String externalUrl, LocalDate observedDate, double benchmarkRating, Product matchedProduct, LocalDateTime createdAt) {
        this.id = id;
        this.storeName = storeName;
        this.productName = productName;
        this.brand = brand;
        this.category = category;
        this.externalPrice = externalPrice;
        this.externalUrl = externalUrl;
        this.observedDate = observedDate;
        this.benchmarkRating = benchmarkRating;
        this.matchedProduct = matchedProduct;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String storeName;
        private String productName;
        private String brand;
        private String category;
        private BigDecimal externalPrice;
        private String externalUrl;
        private LocalDate observedDate;
        private double benchmarkRating;
        private Product matchedProduct;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder storeName(String storeName) { this.storeName = storeName; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder brand(String brand) { this.brand = brand; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder externalPrice(BigDecimal externalPrice) { this.externalPrice = externalPrice; return this; }
        public Builder externalUrl(String externalUrl) { this.externalUrl = externalUrl; return this; }
        public Builder observedDate(LocalDate observedDate) { this.observedDate = observedDate; return this; }
        public Builder benchmarkRating(double benchmarkRating) { this.benchmarkRating = benchmarkRating; return this; }
        public Builder matchedProduct(Product matchedProduct) { this.matchedProduct = matchedProduct; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public MarketProduct build() {
            return new MarketProduct(id, storeName, productName, brand, category, externalPrice, externalUrl, observedDate, benchmarkRating, matchedProduct, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getExternalPrice() { return externalPrice; }
    public void setExternalPrice(BigDecimal externalPrice) { this.externalPrice = externalPrice; }

    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }

    public LocalDate getObservedDate() { return observedDate; }
    public void setObservedDate(LocalDate observedDate) { this.observedDate = observedDate; }

    public double getBenchmarkRating() { return benchmarkRating; }
    public void setBenchmarkRating(double benchmarkRating) { this.benchmarkRating = benchmarkRating; }

    public Product getMatchedProduct() { return matchedProduct; }
    public void setMatchedProduct(Product matchedProduct) { this.matchedProduct = matchedProduct; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
