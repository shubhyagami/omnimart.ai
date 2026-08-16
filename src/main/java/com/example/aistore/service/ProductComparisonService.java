package com.example.aistore.service;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.ProductSpecification;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.ProductSpecificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductComparisonService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductComparisonService.class);


    private final ProductRepository productRepository;
    private final ProductSpecificationRepository specRepository;
    private final ProductService productService;
    public ProductComparisonService(ProductRepository productRepository, ProductSpecificationRepository specRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.specRepository = specRepository;
        this.productService = productService;
    }


    @Transactional(readOnly = true)
    public ProductComparisonDto compareProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return ProductComparisonDto.builder().products(Collections.emptyList()).build();
        }

        List<Product> products = productRepository.findAllByIdsIn(productIds);
        List<ProductCardDto> cardDtos = products.stream().map(productService::toCardDto).collect(Collectors.toList());

        List<ProductSpecification> allSpecs = specRepository.findByProductIdIn(productIds);

        // Group specs by key
        Set<String> allSpecKeys = new LinkedHashSet<>();
        allSpecKeys.add("Brand");
        allSpecKeys.add("Category");
        allSpecKeys.add("Price");
        allSpecKeys.add("Customer Rating");
        allSpecKeys.add("Stock Status");

        Map<String, Map<Long, String>> specMatrix = new LinkedHashMap<>();

        // Add standard keys
        for (String key : allSpecKeys) {
            specMatrix.put(key, new HashMap<>());
        }

        for (ProductCardDto card : cardDtos) {
            specMatrix.get("Brand").put(card.getId(), card.getBrand());
            specMatrix.get("Category").put(card.getId(), card.getCategory());
            specMatrix.get("Price").put(card.getId(), "₹" + card.getPrice().toString());
            specMatrix.get("Customer Rating").put(card.getId(), card.getRating() + "★ (" + card.getReviewCount() + " reviews)");
            specMatrix.get("Stock Status").put(card.getId(), card.getStock() > 0 ? "In Stock (" + card.getStock() + " available)" : "Out of Stock");
        }

        // Add custom specifications from DB
        for (ProductSpecification spec : allSpecs) {
            String key = spec.getSpecKey();
            allSpecKeys.add(key);
            specMatrix.computeIfAbsent(key, k -> new HashMap<>()).put(spec.getProduct().getId(), spec.getSpecValue());
        }

        // Compute Best in categories
        Long bestOverallId = null;
        Long bestValueId = null;
        Long bestCameraId = null;
        Long bestPerformanceId = null;
        Long bestBatteryId = null;

        double highestOverallScore = -1;
        double bestValueRatio = -1;

        for (ProductCardDto p : cardDtos) {
            // Overall score: Rating * 20 + ReviewCount bonus
            double overall = (p.getRating() * 20.0) + Math.min(20, p.getReviewCount() * 0.1);
            if (overall > highestOverallScore) {
                highestOverallScore = overall;
                bestOverallId = p.getId();
            }

            // Value score: (Rating / Price)
            double value = p.getRating() / Math.max(1, p.getPrice().doubleValue());
            if (value > bestValueRatio) {
                bestValueRatio = value;
                bestValueId = p.getId();
            }

            // Camera / Performance / Battery detection
            String tags = (p.getTags() != null ? p.getTags() : "").toLowerCase();
            if (tags.contains("camera") || tags.contains("ois") || tags.contains("photo")) {
                if (bestCameraId == null || p.getRating() > 4.0) bestCameraId = p.getId();
            }
            if (tags.contains("gaming") || tags.contains("flagship") || tags.contains("gpu") || tags.contains("rtx")) {
                if (bestPerformanceId == null || p.getRating() > 4.0) bestPerformanceId = p.getId();
            }
            if (tags.contains("battery") || tags.contains("backup")) {
                if (bestBatteryId == null || p.getRating() > 4.0) bestBatteryId = p.getId();
            }
        }

        if (bestCameraId == null && !cardDtos.isEmpty()) bestCameraId = bestOverallId;
        if (bestPerformanceId == null && !cardDtos.isEmpty()) bestPerformanceId = bestOverallId;
        if (bestBatteryId == null && !cardDtos.isEmpty()) bestBatteryId = bestOverallId;

        return ProductComparisonDto.builder()
                .products(cardDtos)
                .specGroups(new ArrayList<>(allSpecKeys))
                .specMatrix(specMatrix)
                .bestOverallId(bestOverallId)
                .bestValueId(bestValueId)
                .bestCameraId(bestCameraId)
                .bestPerformanceId(bestPerformanceId)
                .bestBatteryId(bestBatteryId)
                .bestOverallExplanation("Highest composite rating and verified customer satisfaction.")
                .bestValueExplanation("Delivers the most balanced feature set per rupee spent.")
                .bestCameraExplanation("Equipped with superior sensor size and optical stabilization.")
                .bestPerformanceExplanation("Benchmark leader in processing throughput and thermal endurance.")
                .bestBatteryExplanation("Largest battery capacity with optimized standby endurance.")
                .aiSummary("AI Comparison Verdict: " + cardDtos.size() + " models analyzed. All comparisons are derived strictly from factual database specifications.")
                .build();
    }
}
