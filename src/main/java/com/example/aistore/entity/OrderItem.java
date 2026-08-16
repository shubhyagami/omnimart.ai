package com.example.aistore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String productName;
    private String productImageUrl;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    public OrderItem() {}

    public OrderItem(Long id, Order order, Product product, String productName, String productImageUrl, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Order order;
        private Product product;
        private String productName;
        private String productImageUrl;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder order(Order order) { this.order = order; return this; }
        public Builder product(Product product) { this.product = product; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder productImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }
        public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }

        public OrderItem build() {
            return new OrderItem(id, order, product, productName, productImageUrl, quantity, unitPrice, totalPrice);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
