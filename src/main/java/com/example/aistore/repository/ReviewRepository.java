package com.example.aistore.repository;

import com.example.aistore.entity.Product;
import com.example.aistore.entity.Review;
import com.example.aistore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrderByCreatedAtDesc(Product product);
    Page<Review> findByProductOrderByCreatedAtDesc(Product product, Pageable pageable);
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    Optional<Review> findByUserAndProduct(User user, Product product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double calculateAverageRating(@Param("productId") Long productId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    int countReviewsByProductId(@Param("productId") Long productId);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.rating")
    List<Object[]> countByRatingForProduct(@Param("productId") Long productId);

    List<Review> findTop10ByOrderByCreatedAtDesc();
}
