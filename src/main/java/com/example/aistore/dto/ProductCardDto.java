package com.example.aistore.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ProductCardDto {
    private Long id;
    private String name;
    private String slug;
    private String brand;
    private String category;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private int discountPercent;
    private double rating;
    private int reviewCount;
    private int stock;
    private String primaryImageUrl;
    private String whyRecommended;
    private String tags;
    private Map<String, String> specsSummary;

    public ProductCardDto() {}

    public ProductCardDto(Long id, String name, String slug, String brand, String category, BigDecimal price, BigDecimal originalPrice, int discountPercent, double rating, int reviewCount, int stock, String primaryImageUrl, String whyRecommended, String tags, Map<String, String> specsSummary) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.stock = stock;
        this.primaryImageUrl = primaryImageUrl;
        this.whyRecommended = whyRecommended;
        this.tags = tags;
        this.specsSummary = specsSummary;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String slug;
        private String brand;
        private String category;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private int discountPercent;
        private double rating;
        private int reviewCount;
        private int stock;
        private String primaryImageUrl;
        private String whyRecommended;
        private String tags;
        private Map<String, String> specsSummary;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder slug(String slug) { this.slug = slug; return this; }
        public Builder brand(String brand) { this.brand = brand; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public Builder discountPercent(int discountPercent) { this.discountPercent = discountPercent; return this; }
        public Builder rating(double rating) { this.rating = rating; return this; }
        public Builder reviewCount(int reviewCount) { this.reviewCount = reviewCount; return this; }
        public Builder stock(int stock) { this.stock = stock; return this; }
        public Builder primaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; return this; }
        public Builder whyRecommended(String whyRecommended) { this.whyRecommended = whyRecommended; return this; }
        public Builder tags(String tags) { this.tags = tags; return this; }
        public Builder specsSummary(Map<String, String> specsSummary) { this.specsSummary = specsSummary; return this; }

        public ProductCardDto build() {
            return new ProductCardDto(id, name, slug, brand, category, price, originalPrice, discountPercent, rating, reviewCount, stock, primaryImageUrl, whyRecommended, tags, specsSummary);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public int getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

    public String getWhyRecommended() { return whyRecommended; }
    public void setWhyRecommended(String whyRecommended) { this.whyRecommended = whyRecommended; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Map<String, String> getSpecsSummary() { return specsSummary; }
    public void setSpecsSummary(Map<String, String> specsSummary) { this.specsSummary = specsSummary; }
}
