package com.example.aistore.repository;

import com.example.aistore.entity.Brand;
import com.example.aistore.entity.Category;
import com.example.aistore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByBrandAndActiveTrue(Brand brand);

    List<Product> findByFeaturedTrueAndActiveTrue();

    List<Product> findTop8ByActiveTrueOrderByRatingDesc();

    List<Product> findTop8ByActiveTrueOrderByCreatedAtDesc();

    List<Product> findTop10ByActiveTrueOrderByReviewCountDesc();

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.tags) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.keywords) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.brand.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchProducts(@Param("query") String query);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:minRating IS NULL OR p.rating >= :minRating) AND " +
           "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.tags) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> filterProducts(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRating") Double minRating,
            @Param("query") String query,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category = :category AND p.id <> :excludeId ORDER BY p.rating DESC")
    List<Product> findRelatedProducts(@Param("category") Category category, @Param("excludeId") Long excludeId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIdsIn(@Param("ids") List<Long> ids);
}
