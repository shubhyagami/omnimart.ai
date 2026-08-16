package com.example.aistore.service;

import com.example.aistore.dto.CheckoutRequestDto;
import com.example.aistore.entity.*;
import com.example.aistore.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);


    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final UserInteractionService interactionService;
    private final com.example.aistore.service.email.BrevoEmailService brevoEmailService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, AddressRepository addressRepository, ProductRepository productRepository, PaymentService paymentService, UserInteractionService interactionService, com.example.aistore.service.email.BrevoEmailService brevoEmailService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
        this.interactionService = interactionService;
        this.brevoEmailService = brevoEmailService;
    }


    @Transactional
    public Order createOrderFromCart(User user, CheckoutRequestDto checkoutDto) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalStateException("Cart is empty"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart");
        }

        // Save or update shipping address
        Address address;
        if (checkoutDto.getAddressId() != null) {
            address = addressRepository.findById(checkoutDto.getAddressId()).orElse(null);
        } else {
            address = Address.builder()
                    .user(user)
                    .fullName(checkoutDto.getFullName())
                    .streetAddress(checkoutDto.getStreetAddress())
                    .apartment(checkoutDto.getApartment())
                    .city(checkoutDto.getCity())
                    .state(checkoutDto.getState())
                    .postalCode(checkoutDto.getPostalCode())
                    .country(checkoutDto.getCountry())
                    .phone(checkoutDto.getPhone())
                    .addressType("HOME")
                    .isDefault(true)
                    .build();
            address = addressRepository.save(address);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            subtotal = subtotal.add(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        BigDecimal shippingFee = subtotal.compareTo(BigDecimal.valueOf(500)) >= 0 ? BigDecimal.ZERO : BigDecimal.valueOf(40);
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18)); // 18% GST/Tax
        BigDecimal finalAmount = subtotal.add(shippingFee).add(tax);

        String orderNum = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String trackingNum = "TRK-EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNum)
                .user(user)
                .totalAmount(subtotal)
                .discountAmount(BigDecimal.ZERO)
                .taxAmount(tax)
                .finalAmount(finalAmount)
                .status(Order.OrderStatus.CONFIRMED)
                .shippingAddress(address)
                .carrier("OmniExpress Logistics")
                .trackingNumber(trackingNum)
                .build();

        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            BigDecimal itemTotal = ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .productName(p.getName())
                    .productImageUrl(p.getPrimaryImageUrl())
                    .quantity(ci.getQuantity())
                    .unitPrice(ci.getUnitPrice())
                    .totalPrice(itemTotal)
                    .build();
            orderItems.add(oi);
            orderItemRepository.save(oi);

            // Deduct stock
            int newStock = Math.max(0, p.getStock() - ci.getQuantity());
            p.setStock(newStock);
            productRepository.save(p);

            // Behavioral tracking for purchase event
            interactionService.recordInteraction(user.getId(), null, UserInteraction.InteractionType.PRODUCT_PURCHASE, p.getId(), 0, null);
        }

        order.setItems(orderItems);

        // Process Payment
        Payment payment = paymentService.processPayment(order, checkoutDto.getPaymentMethod(), finalAmount);
        order.setPayment(payment);

        // Clear Cart
        cartItemRepository.deleteByCart(cart);

        Order savedOrder = orderRepository.save(order);

        // Send Order Confirmation Notification via Brevo Email API
        try {
            StringBuilder itemsSummary = new StringBuilder();
            for (OrderItem item : orderItems) {
                if (itemsSummary.length() > 0) itemsSummary.append(", ");
                itemsSummary.append(item.getProductName()).append(" (x").append(item.getQuantity()).append(")");
            }
            brevoEmailService.sendOrderConfirmationEmail(
                    user.getEmail(),
                    user.getFullName(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getFinalAmount(),
                    itemsSummary.toString()
            );
        } catch (Exception e) {
            log.warn("Failed to dispatch order confirmation email via Brevo: {}", e.getMessage());
        }

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderByIdAndUser(Long id, User user) {
        return orderRepository.findByIdAndUser(id, user);
    }

    @Transactional
    public Order cancelOrder(Long orderId, User user, String reason) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() == Order.OrderStatus.DELIVERED || order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be cancelled in status: " + order.getStatus());
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancellationReason(reason);

        // Restore stock
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct() != null) {
                Product p = oi.getProduct();
                p.setStock(p.getStock() + oi.getQuantity());
                productRepository.save(p);
            }
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order requestReturn(Long orderId, User user, String returnReason) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() != Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Returns can only be requested for delivered orders");
        }

        order.setStatus(Order.OrderStatus.RETURN_REQUESTED);
        order.setReturnReason(returnReason);
        order.setReturnStatus("REQUESTED");

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(newStatus);
        if (newStatus == Order.OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllOrdersPaged(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
