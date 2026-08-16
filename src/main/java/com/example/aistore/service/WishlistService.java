package com.example.aistore.service;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.*;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.WishlistItemRepository;
import com.example.aistore.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserInteractionService interactionService;
    public WishlistService(WishlistRepository wishlistRepository, WishlistItemRepository wishlistItemRepository, ProductRepository productRepository, ProductService productService, UserInteractionService interactionService) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.interactionService = interactionService;
    }


    @Transactional
    public Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> wishlistRepository.save(Wishlist.builder().user(user).build()));
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getUserWishlistProducts(User user) {
        Optional<Wishlist> wishlist = wishlistRepository.findByUser(user);
        if (wishlist.isEmpty()) return Collections.emptyList();

        List<WishlistItem> items = wishlistItemRepository.findByWishlist(wishlist.get());
        return items.stream()
                .map(item -> productService.toCardDto(item.getProduct()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean toggleWishlist(User user, Long productId) {
        Wishlist wishlist = getOrCreateWishlist(user);
        Product product = productRepository.findById(productId).orElseThrow();

        Optional<WishlistItem> existing = wishlistItemRepository.findByWishlistAndProduct(wishlist, product);
        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get());
            return false; // Removed
        } else {
            WishlistItem item = WishlistItem.builder().wishlist(wishlist).product(product).build();
            wishlistItemRepository.save(item);
            interactionService.recordInteraction(user.getId(), null, UserInteraction.InteractionType.WISHLIST_ADD, productId, 0, null);
            return true; // Added
        }
    }

    @Transactional(readOnly = true)
    public boolean isInWishlist(User user, Long productId) {
        if (user == null || productId == null) return false;
        Optional<Wishlist> wishlist = wishlistRepository.findByUser(user);
        if (wishlist.isEmpty()) return false;
        return productRepository.findById(productId)
                .map(p -> wishlistItemRepository.existsByWishlistAndProduct(wishlist.get(), p))
                .orElse(false);
    }
}
