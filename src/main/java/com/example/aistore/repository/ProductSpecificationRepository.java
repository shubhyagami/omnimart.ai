package com.example.aistore.repository;

import com.example.aistore.entity.Product;
import com.example.aistore.entity.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findByProduct(Product product);
    List<ProductSpecification> findByProductIdIn(List<Long> productIds);
}
