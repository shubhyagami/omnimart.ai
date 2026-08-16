package com.example.aistore.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private BigDecimal shippingFee = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private int totalQuantity = 0;

    public CartDto() {}

    public CartDto(Long id, List<CartItemDto> items, BigDecimal subtotal, BigDecimal discountAmount, BigDecimal shippingFee, BigDecimal tax, BigDecimal totalAmount, int totalQuantity) {
        this.id = id;
        this.items = items != null ? items : new ArrayList<>();
        this.subtotal = subtotal != null ? subtotal : BigDecimal.ZERO;
        this.discountAmount = discountAmount != null ? discountAmount : BigDecimal.ZERO;
        this.shippingFee = shippingFee != null ? shippingFee : BigDecimal.ZERO;
        this.tax = tax != null ? tax : BigDecimal.ZERO;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        this.totalQuantity = totalQuantity;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private List<CartItemDto> items = new ArrayList<>();
        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal discountAmount = BigDecimal.ZERO;
        private BigDecimal shippingFee = BigDecimal.ZERO;
        private BigDecimal tax = BigDecimal.ZERO;
        private BigDecimal totalAmount = BigDecimal.ZERO;
        private int totalQuantity = 0;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder items(List<CartItemDto> items) { this.items = items; return this; }
        public Builder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
        public Builder discount(BigDecimal discount) { this.discountAmount = discount; return this; }
        public Builder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder shippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; return this; }
        public Builder tax(BigDecimal tax) { this.tax = tax; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder totalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; return this; }

        public CartDto build() {
            return new CartDto(id, items, subtotal, discountAmount, shippingFee, tax, totalAmount, totalQuantity);
        }
    }

    public static class CartItemDto {
        private Long id;
        private Long productId;
        private String productName;
        private String productSlug;
        private String primaryImageUrl;
        private String brandName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal originalPrice;
        private int discountPercent;
        private BigDecimal totalPrice;
        private int stock;

        public CartItemDto() {}

        public CartItemDto(Long id, Long productId, String productName, String productSlug, String primaryImageUrl, String brandName, int quantity, BigDecimal unitPrice, BigDecimal originalPrice, int discountPercent, BigDecimal totalPrice, int stock) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.productSlug = productSlug;
            this.primaryImageUrl = primaryImageUrl;
            this.brandName = brandName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.originalPrice = originalPrice;
            this.discountPercent = discountPercent;
            this.totalPrice = totalPrice;
            this.stock = stock;
        }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private Long id;
            private Long productId;
            private String productName;
            private String productSlug;
            private String primaryImageUrl;
            private String brandName;
            private int quantity;
            private BigDecimal unitPrice;
            private BigDecimal originalPrice;
            private int discountPercent;
            private BigDecimal totalPrice;
            private int stock;

            public Builder id(Long id) { this.id = id; return this; }
            public Builder productId(Long productId) { this.productId = productId; return this; }
            public Builder productName(String productName) { this.productName = productName; return this; }
            public Builder productSlug(String productSlug) { this.productSlug = productSlug; return this; }
            public Builder primaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; return this; }
            public Builder brandName(String brandName) { this.brandName = brandName; return this; }
            public Builder quantity(int quantity) { this.quantity = quantity; return this; }
            public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
            public Builder originalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; return this; }
            public Builder discountPercent(int discountPercent) { this.discountPercent = discountPercent; return this; }
            public Builder totalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; return this; }
            public Builder stock(int stock) { this.stock = stock; return this; }

            public CartItemDto build() {
                return new CartItemDto(id, productId, productName, productSlug, primaryImageUrl, brandName, quantity, unitPrice, originalPrice, discountPercent, totalPrice, stock);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getProductSlug() { return productSlug; }
        public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

        public String getPrimaryImageUrl() { return primaryImageUrl; }
        public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

        public String getBrandName() { return brandName; }
        public void setBrandName(String brandName) { this.brandName = brandName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getOriginalPrice() { return originalPrice; }
        public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

        public int getDiscountPercent() { return discountPercent; }
        public void setDiscountPercent(int discountPercent) { this.discountPercent = discountPercent; }

        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = shippingFee; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
}
