package com.example.aistore.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductFilterDto {
    private String query;
    private Long categoryId;
    private Long brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Double minRating;
    private String sortBy; // featured, price_asc, price_desc, rating, newest
    private List<String> requiredFeatures;
    private Integer page = 0;
    private Integer size = 12;

    public ProductFilterDto() {}

    public ProductFilterDto(String query, Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, String sortBy, List<String> requiredFeatures, Integer page, Integer size) {
        this.query = query;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minRating = minRating;
        this.sortBy = sortBy;
        this.requiredFeatures = requiredFeatures;
        this.page = page != null ? page : 0;
        this.size = size != null ? size : 12;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String query;
        private Long categoryId;
        private Long brandId;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Double minRating;
        private String sortBy;
        private List<String> requiredFeatures;
        private Integer page = 0;
        private Integer size = 12;

        public Builder query(String query) { this.query = query; return this; }
        public Builder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public Builder brandId(Long brandId) { this.brandId = brandId; return this; }
        public Builder minPrice(BigDecimal minPrice) { this.minPrice = minPrice; return this; }
        public Builder maxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; return this; }
        public Builder minRating(Double minRating) { this.minRating = minRating; return this; }
        public Builder sortBy(String sortBy) { this.sortBy = sortBy; return this; }
        public Builder requiredFeatures(List<String> requiredFeatures) { this.requiredFeatures = requiredFeatures; return this; }
        public Builder page(Integer page) { this.page = page; return this; }
        public Builder size(Integer size) { this.size = size; return this; }

        public ProductFilterDto build() {
            return new ProductFilterDto(query, categoryId, brandId, minPrice, maxPrice, minRating, sortBy, requiredFeatures, page, size);
        }
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public List<String> getRequiredFeatures() { return requiredFeatures; }
    public void setRequiredFeatures(List<String> requiredFeatures) { this.requiredFeatures = requiredFeatures; }

    public Integer getPage() { return page != null ? page : 0; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size != null ? size : 12; }
    public void setSize(Integer size) { this.size = size; }
}
