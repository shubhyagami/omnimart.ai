package com.example.aistore.repository;

import com.example.aistore.entity.Inventory;
import com.example.aistore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct(Product product);
    Optional<Inventory> findByProductId(Long productId);
}
