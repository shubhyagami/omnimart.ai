package com.example.aistore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String imageUrl;

    private boolean isPrimary;
    private String altText;

    public ProductImage() {}

    public ProductImage(Long id, Product product, String imageUrl, boolean isPrimary, String altText) {
        this.id = id;
        this.product = product;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.altText = altText;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Product product;
        private String imageUrl;
        private boolean isPrimary;
        private String altText;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder isPrimary(boolean isPrimary) { this.isPrimary = isPrimary; return this; }
        public Builder altText(String altText) { this.altText = altText; return this; }

        public ProductImage build() {
            return new ProductImage(id, product, imageUrl, isPrimary, altText);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPrimary() { return isPrimary; }
    public void setPrimary(boolean primary) { isPrimary = primary; }

    public String getAltText() { return altText; }
    public void setAltText(String altText) { this.altText = altText; }
}
