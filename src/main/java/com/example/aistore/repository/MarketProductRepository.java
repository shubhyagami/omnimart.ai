package com.example.aistore.repository;

import com.example.aistore.entity.MarketProduct;
import com.example.aistore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketProductRepository extends JpaRepository<MarketProduct, Long> {
    List<MarketProduct> findByMatchedProduct(Product matchedProduct);
    List<MarketProduct> findByCategory(String category);
}
