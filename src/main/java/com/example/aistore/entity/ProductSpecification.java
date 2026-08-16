package com.example.aistore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_specifications")
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "spec_group")
    private String specGroup;

    @Column(nullable = false)
    private String specKey;

    @Column(nullable = false)
    private String specValue;

    public ProductSpecification() {}

    public ProductSpecification(Long id, Product product, String specGroup, String specKey, String specValue) {
        this.id = id;
        this.product = product;
        this.specGroup = specGroup;
        this.specKey = specKey;
        this.specValue = specValue;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Product product;
        private String specGroup;
        private String specKey;
        private String specValue;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder specGroup(String specGroup) { this.specGroup = specGroup; return this; }
        public Builder specKey(String specKey) { this.specKey = specKey; return this; }
        public Builder specValue(String specValue) { this.specValue = specValue; return this; }

        public ProductSpecification build() {
            return new ProductSpecification(id, product, specGroup, specKey, specValue);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getSpecGroup() { return specGroup; }
    public void setSpecGroup(String specGroup) { this.specGroup = specGroup; }

    public String getSpecKey() { return specKey; }
    public void setSpecKey(String specKey) { this.specKey = specKey; }

    public String getSpecValue() { return specValue; }
    public void setSpecValue(String specValue) { this.specValue = specValue; }
}
