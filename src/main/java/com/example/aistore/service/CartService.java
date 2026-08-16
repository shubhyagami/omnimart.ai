package com.example.aistore.service;

import com.example.aistore.dto.CartDto;
import com.example.aistore.entity.*;
import com.example.aistore.repository.CartItemRepository;
import com.example.aistore.repository.CartRepository;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserInteractionService interactionService;
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository, UserInteractionService interactionService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.interactionService = interactionService;
    }


    @Transactional
    public Cart getOrCreateCart(Long userId, String sessionId) {
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                return cartRepository.findByUser(user)
                        .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
            }
        }

        if (sessionId != null) {
            return cartRepository.findBySessionId(sessionId)
                    .orElseGet(() -> cartRepository.save(Cart.builder().sessionId(sessionId).build()));
        }

        return cartRepository.save(Cart.builder().build());
    }

    @Transactional
    public CartDto getCartDto(Long userId, String sessionId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        return mapToDto(cart);
    }

    @Transactional
    public CartDto addToCart(Long userId, String sessionId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId, sessionId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Optional<CartItem> existing = cartItemRepository.findByCartAndProduct(cart, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .build();
            cartItemRepository.save(newItem);
        }

        // Record behavioral telemetry
        interactionService.recordInteraction(userId, sessionId, UserInteraction.InteractionType.CART_ADD, productId, 0, null);

        return getCartDto(userId, sessionId);
    }

    @Transactional
    public CartDto updateQuantity(Long userId, String sessionId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId, sessionId);
        Product product = productRepository.findById(productId).orElseThrow();

        Optional<CartItem> existing = cartItemRepository.findByCartAndProduct(cart, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            if (quantity <= 0) {
                cartItemRepository.delete(item);
                interactionService.recordInteraction(userId, sessionId, UserInteraction.InteractionType.CART_REMOVE, productId, 0, null);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
        return getCartDto(userId, sessionId);
    }

    @Transactional
    public CartDto removeFromCart(Long userId, String sessionId, Long productId) {
        return updateQuantity(userId, sessionId, productId, 0);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }

    private CartDto mapToDto(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);
        List<CartDto.CartItemDto> itemDtos = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;
        int totalQty = 0;

        for (CartItem item : items) {
            Product p = item.getProduct();
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
            totalQty += item.getQuantity();

            itemDtos.add(CartDto.CartItemDto.builder()
                    .id(item.getId())
                    .productId(p.getId())
                    .productName(p.getName())
                    .productSlug(p.getSlug())
                    .primaryImageUrl(p.getPrimaryImageUrl())
                    .unitPrice(item.getUnitPrice())
                    .originalPrice(p.getOriginalPrice())
                    .discountPercent(p.getDiscountPercent())
                    .quantity(item.getQuantity())
                    .totalPrice(itemTotal)
                    .stock(p.getStock())
                    .brandName(p.getBrand() != null ? p.getBrand().getName() : "")
                    .build());
        }

        BigDecimal shippingFee = subtotal.compareTo(BigDecimal.valueOf(500)) >= 0 || items.isEmpty()
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(40);
        BigDecimal totalAmount = subtotal.add(shippingFee);

        return CartDto.builder()
                .id(cart.getId())
                .items(itemDtos)
                .totalQuantity(totalQty)
                .subtotal(subtotal)
                .discountAmount(BigDecimal.ZERO)
                .shippingFee(shippingFee)
                .totalAmount(totalAmount)
                .build();
    }
}
