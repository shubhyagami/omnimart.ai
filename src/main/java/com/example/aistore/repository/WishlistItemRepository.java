package com.example.aistore.repository;

import com.example.aistore.entity.Product;
import com.example.aistore.entity.Wishlist;
import com.example.aistore.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByWishlist(Wishlist wishlist);
    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);
    boolean existsByWishlistAndProduct(Wishlist wishlist, Product product);
    void deleteByWishlistAndProduct(Wishlist wishlist, Product product);
}
