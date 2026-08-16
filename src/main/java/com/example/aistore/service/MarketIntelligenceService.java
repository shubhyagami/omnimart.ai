package com.example.aistore.service;

import com.example.aistore.entity.MarketProduct;
import com.example.aistore.entity.Product;
import com.example.aistore.repository.MarketProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarketIntelligenceService {

    private final MarketProductRepository marketProductRepository;
    public MarketIntelligenceService(MarketProductRepository marketProductRepository) {
        this.marketProductRepository = marketProductRepository;
    }


    @Transactional(readOnly = true)
    public List<MarketProduct> getCompetitorPricesForProduct(Product product) {
        return marketProductRepository.findByMatchedProduct(product);
    }

    @Transactional(readOnly = true)
    public List<MarketProduct> getAllMarketBenchmarks() {
        return marketProductRepository.findAll();
    }
}
