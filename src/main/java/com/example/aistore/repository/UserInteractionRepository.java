package com.example.aistore.repository;

import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    List<UserInteraction> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<UserInteraction> findBySessionIdOrderByCreatedAtDesc(String sessionId, Pageable pageable);

    @Query("SELECT ui.categoryName, COUNT(ui) FROM UserInteraction ui WHERE ui.user.id = :userId AND ui.categoryName IS NOT NULL GROUP BY ui.categoryName ORDER BY COUNT(ui) DESC")
    List<Object[]> getUserCategoryAffinity(@Param("userId") Long userId);

    @Query("SELECT ui.brandName, COUNT(ui) FROM UserInteraction ui WHERE ui.user.id = :userId AND ui.brandName IS NOT NULL GROUP BY ui.brandName ORDER BY COUNT(ui) DESC")
    List<Object[]> getUserBrandAffinity(@Param("userId") Long userId);

    @Query("SELECT AVG(ui.priceAtEvent), MIN(ui.priceAtEvent), MAX(ui.priceAtEvent) FROM UserInteraction ui WHERE ui.user.id = :userId AND ui.priceAtEvent IS NOT NULL")
    List<Object[]> getUserPriceRangeStats(@Param("userId") Long userId);

    @Query("SELECT ui.product.id, ui.product.name, COUNT(ui) FROM UserInteraction ui WHERE ui.eventType = 'PRODUCT_VIEW' GROUP BY ui.product.id, ui.product.name ORDER BY COUNT(ui) DESC")
    List<Object[]> findMostViewedProducts(Pageable pageable);

    @Query("SELECT ui.product.id, ui.product.name, COUNT(ui) FROM UserInteraction ui WHERE ui.eventType = 'CART_ADD' GROUP BY ui.product.id, ui.product.name ORDER BY COUNT(ui) DESC")
    List<Object[]> findMostCartAddedProducts(Pageable pageable);

    @Query("SELECT ui.eventType, COUNT(ui) FROM UserInteraction ui GROUP BY ui.eventType")
    List<Object[]> countByEventType();

    @Query("SELECT DISTINCT ui.product.id FROM UserInteraction ui WHERE ui.user.id = :userId AND ui.eventType IN ('PRODUCT_VIEW', 'PRODUCT_CLICK', 'CART_ADD') ORDER BY ui.createdAt DESC")
    List<Long> findRecentlyInteractedProductIds(@Param("userId") Long userId, Pageable pageable);
}
