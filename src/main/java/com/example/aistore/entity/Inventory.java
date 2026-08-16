package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    private int stockQuantity;
    private int reservedQuantity = 0;
    private String warehouseLocation;

    @UpdateTimestamp
    private LocalDateTime lastRestockedAt;

    public Inventory() {}

    public Inventory(Long id, Product product, int stockQuantity, int reservedQuantity, String warehouseLocation, LocalDateTime lastRestockedAt) {
        this.id = id;
        this.product = product;
        this.stockQuantity = stockQuantity;
        this.reservedQuantity = reservedQuantity;
        this.warehouseLocation = warehouseLocation;
        this.lastRestockedAt = lastRestockedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Product product;
        private int stockQuantity;
        private int reservedQuantity = 0;
        private String warehouseLocation;
        private LocalDateTime lastRestockedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder stockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public Builder reservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }
        public Builder warehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; return this; }
        public Builder lastRestockedAt(LocalDateTime lastRestockedAt) { this.lastRestockedAt = lastRestockedAt; return this; }

        public Inventory build() {
            return new Inventory(id, product, stockQuantity, reservedQuantity, warehouseLocation, lastRestockedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public String getWarehouseLocation() { return warehouseLocation; }
    public void setWarehouseLocation(String warehouseLocation) { this.warehouseLocation = warehouseLocation; }

    public LocalDateTime getLastRestockedAt() { return lastRestockedAt; }
    public void setLastRestockedAt(LocalDateTime lastRestockedAt) { this.lastRestockedAt = lastRestockedAt; }
}
