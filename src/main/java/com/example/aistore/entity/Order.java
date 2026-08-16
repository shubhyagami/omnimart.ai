package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED,
        RETURN_REQUESTED,
        RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    private String trackingNumber;
    private String carrier;

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(columnDefinition = "TEXT")
    private String returnReason;

    private String returnStatus;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    public Order() {}

    public Order(Long id, String orderNumber, User user, List<OrderItem> items, BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal taxAmount, BigDecimal finalAmount, OrderStatus status, Payment payment, Address shippingAddress, String trackingNumber, String carrier, String cancellationReason, String returnReason, String returnStatus, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deliveredAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.user = user;
        this.items = items != null ? items : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.taxAmount = taxAmount;
        this.finalAmount = finalAmount;
        this.status = status != null ? status : OrderStatus.PENDING;
        this.payment = payment;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.cancellationReason = cancellationReason;
        this.returnReason = returnReason;
        this.returnStatus = returnStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deliveredAt = deliveredAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String orderNumber;
        private User user;
        private List<OrderItem> items = new ArrayList<>();
        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal finalAmount;
        private OrderStatus status = OrderStatus.PENDING;
        private Payment payment;
        private Address shippingAddress;
        private String trackingNumber;
        private String carrier;
        private String cancellationReason;
        private String returnReason;
        private String returnStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deliveredAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder items(List<OrderItem> items) { this.items = items; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
        public Builder status(OrderStatus status) { this.status = status; return this; }
        public Builder payment(Payment payment) { this.payment = payment; return this; }
        public Builder shippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; return this; }
        public Builder trackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; return this; }
        public Builder carrier(String carrier) { this.carrier = carrier; return this; }
        public Builder cancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; return this; }
        public Builder returnReason(String returnReason) { this.returnReason = returnReason; return this; }
        public Builder returnStatus(String returnStatus) { this.returnStatus = returnStatus; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }

        public Order build() {
            return new Order(id, orderNumber, user, items, totalAmount, discountAmount, taxAmount, finalAmount, status, payment, shippingAddress, trackingNumber, carrier, cancellationReason, returnReason, returnStatus, createdAt, updatedAt, deliveredAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }

    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}
