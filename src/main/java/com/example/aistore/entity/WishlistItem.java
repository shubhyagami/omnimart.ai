package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @CreationTimestamp
    private LocalDateTime addedAt;

    public WishlistItem() {}

    public WishlistItem(Long id, Wishlist wishlist, Product product, LocalDateTime addedAt) {
        this.id = id;
        this.wishlist = wishlist;
        this.product = product;
        this.addedAt = addedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Wishlist wishlist;
        private Product product;
        private LocalDateTime addedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder wishlist(Wishlist wishlist) { this.wishlist = wishlist; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder addedAt(LocalDateTime addedAt) { this.addedAt = addedAt; return this; }

        public WishlistItem build() {
            return new WishlistItem(id, wishlist, product, addedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Wishlist getWishlist() { return wishlist; }
    public void setWishlist(Wishlist wishlist) { this.wishlist = wishlist; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
