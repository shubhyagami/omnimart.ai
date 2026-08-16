package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detailedSpecsJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 12, scale = 2)
    private BigDecimal originalPrice;

    private int discountPercent;
    private double rating = 0.0;
    private int reviewCount = 0;
    private int stock = 0;
    private boolean active = true;
    private boolean featured = false;
    private String primaryImageUrl;
    private String tags;
    private String keywords;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSpecification> specifications = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Inventory inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(Long id, String name, String slug, String description, String detailedSpecsJson, Brand brand, Category category, BigDecimal price, BigDecimal originalPrice, int discountPercent, double rating, int reviewCount, int stock, boolean active, boolean featured, String primaryImageUrl, String tags, String keywords, List<ProductImage> images, List<ProductSpecification> specifications, Inventory inventory, List<Review> reviews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.detailedSpecsJson = detailedSpecsJson;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.stock = stock;
        this.active = active;
        this.featured = featured;
        this.primaryImageUrl = primaryImageUrl;
        this.tags = tags;
        this.keywords = keywords;
        this.images = images != null ? images : new ArrayList<>();
        this.specifications = specifications != null ? specifications : new ArrayList<>();
        this.inventory = inventory;
        this.reviews = reviews != null ? reviews : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private String detailedSpecsJson;
        private Brand brand;
        private Category category;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private int discountPercent;
        private double rating = 0.0;
        private int reviewCount = 0;
        private int stock = 0;
        private boolean active = true;
        private boolean featured = false;
        private String primaryImageUrl;
        private String tags;
        private String keywords;
        private List<ProductImage> images = new ArrayList<>();
        private List<ProductSpecification> specifications = new ArrayList<>();
        private Inventory inventory;
        private List<Review> reviews = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder slug(String slug) { this.slug = slug; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder detailedSpecsJson(String detailedSpecsJson) { this.detailedSpecsJson = detailedSpecsJson; return this; }
        public Builder brand(Brand brand) { this.brand = brand; return this; }
        public Builder category(Category category) { this.category = category; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
        public Builder discountPercent(int discountPercent) { this.discountPercent = discountPercent; return this; }
        public Builder rating(double rating) { this.rating = rating; return this; }
        public Builder reviewCount(int reviewCount) { this.reviewCount = reviewCount; return this; }
        public Builder stock(int stock) { this.stock = stock; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder featured(boolean featured) { this.featured = featured; return this; }
        public Builder primaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; return this; }
        public Builder tags(String tags) { this.tags = tags; return this; }
        public Builder keywords(String keywords) { this.keywords = keywords; return this; }
        public Builder images(List<ProductImage> images) { this.images = images; return this; }
        public Builder specifications(List<ProductSpecification> specifications) { this.specifications = specifications; return this; }
        public Builder inventory(Inventory inventory) { this.inventory = inventory; return this; }
        public Builder reviews(List<Review> reviews) { this.reviews = reviews; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Product build() {
            return new Product(id, name, slug, description, detailedSpecsJson, brand, category, price, originalPrice, discountPercent, rating, reviewCount, stock, active, featured, primaryImageUrl, tags, keywords, images, specifications, inventory, reviews, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDetailedSpecsJson() { return detailedSpecsJson; }
    public void setDetailedSpecsJson(String detailedSpecsJson) { this.detailedSpecsJson = detailedSpecsJson; }

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }

    public List<ProductSpecification> getSpecifications() { return specifications; }
    public void setSpecifications(List<ProductSpecification> specifications) { this.specifications = specifications; }

    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
