package com.example.aistore.repository;

import com.example.aistore.entity.ProductComparison;
import com.example.aistore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductComparisonRepository extends JpaRepository<ProductComparison, Long> {
    List<ProductComparison> findByUserOrderByCreatedAtDesc(User user);
}
