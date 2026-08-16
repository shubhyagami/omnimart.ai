package com.example.aistore.service;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductFilterDto;
import com.example.aistore.entity.Category;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.ProductSpecification;
import com.example.aistore.repository.CategoryRepository;
import com.example.aistore.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductService.class);


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }


    @Transactional(readOnly = true)
    public Page<ProductCardDto> filterProducts(ProductFilterDto filter) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        if ("price_asc".equalsIgnoreCase(filter.getSortBy())) {
            sort = Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equalsIgnoreCase(filter.getSortBy())) {
            sort = Sort.by(Sort.Direction.DESC, "price");
        } else if ("newest".equalsIgnoreCase(filter.getSortBy())) {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        Pageable pageable = PageRequest.of(Math.max(0, filter.getPage()), Math.max(1, filter.getSize()), sort);

        Page<Product> page = productRepository.filterProducts(
                filter.getCategoryId(),
                filter.getBrandId(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getMinRating(),
                (filter.getQuery() != null && !filter.getQuery().trim().isEmpty()) ? filter.getQuery().trim() : null,
                pageable
        );

        return page.map(this::toCardDto);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> searchProducts(String query, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, int limit) {
        return searchProducts(query, categoryId, null, minPrice, maxPrice, minRating, Collections.emptyList(), limit);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> searchProducts(String query, Long categoryId, Long brandId, BigDecimal minPrice, BigDecimal maxPrice, Double minRating, List<String> features, int limit) {
        List<Product> list = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            list.addAll(productRepository.searchProducts(query.trim()));
        }
        if (categoryId != null) {
            Category cat = categoryRepository.findById(categoryId).orElse(null);
            if (cat != null) {
                List<Product> catList = productRepository.findByCategoryAndActiveTrue(cat);
                Set<Long> existingIds = list.stream().map(Product::getId).collect(Collectors.toSet());
                for (Product p : catList) {
                    if (!existingIds.contains(p.getId())) {
                        list.add(p);
                    }
                }
            }
        }
        if (list.isEmpty()) {
            list = productRepository.findAll();
        }

        List<String> requiredFeatures = features != null ? features : Collections.emptyList();

        return list.stream()
                .filter(Product::isActive)
                .filter(p -> categoryId == null || (p.getCategory() != null && p.getCategory().getId().equals(categoryId)))
                .filter(p -> brandId == null || (p.getBrand() != null && p.getBrand().getId().equals(brandId)))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .filter(p -> minRating == null || p.getRating() >= (minRating - 0.5)) // allow slight tolerance for best products within budget
                .sorted((p1, p2) -> {
                    // Feature relevance score
                    int score1 = calculateFeatureMatchScore(p1, requiredFeatures);
                    int score2 = calculateFeatureMatchScore(p2, requiredFeatures);
                    if (score1 != score2) {
                        return Integer.compare(score2, score1);
                    }
                    // Sort by rating descending
                    return Double.compare(p2.getRating(), p1.getRating());
                })
                .limit(limit > 0 ? limit : 10)
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    private int calculateFeatureMatchScore(Product p, List<String> features) {
        if (features == null || features.isEmpty()) return 0;
        int score = 0;
        String combined = (p.getName() + " " + p.getDescription() + " " + p.getTags() + " " + p.getKeywords() + " " + p.getDetailedSpecsJson()).toLowerCase();
        for (String f : features) {
            String fLower = f.toLowerCase();
            if (combined.contains(fLower)) score += 5;
            if (fLower.equals("camera") && (combined.contains("mp") || combined.contains("ois") || combined.contains("sony lyt") || combined.contains("sensor"))) score += 3;
            if (fLower.equals("gaming") && (combined.contains("rtx") || combined.contains("gpu") || combined.contains("144hz") || combined.contains("165hz"))) score += 3;
            if (fLower.equals("battery") && (combined.contains("mah") || combined.contains("fast charge") || combined.contains("supervooc"))) score += 3;
        }
        return score;
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getFeaturedProducts() {
        return productRepository.findByFeaturedTrueAndActiveTrue().stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getTopRatedProducts() {
        return productRepository.findTop8ByActiveTrueOrderByRatingDesc().stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getNewArrivals() {
        return productRepository.findTop8ByActiveTrueOrderByCreatedAtDesc().stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getTrendingProducts() {
        return productRepository.findTop10ByActiveTrueOrderByReviewCountDesc().stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getRelatedProducts(Product product, int limit) {
        if (product.getCategory() == null) return Collections.emptyList();
        return productRepository.findRelatedProducts(product.getCategory(), product.getId(), PageRequest.of(0, limit)).stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductCardDto> getProductsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        return productRepository.findAllByIdsIn(ids).stream()
                .map(this::toCardDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean checkStockAvailability(Long productId, int quantity) {
        return productRepository.findById(productId)
                .map(p -> p.getStock() >= quantity)
                .orElse(false);
    }

    public ProductCardDto toCardDto(Product product) {
        Map<String, String> specs = new LinkedHashMap<>();
        if (product.getSpecifications() != null && !product.getSpecifications().isEmpty()) {
            for (ProductSpecification spec : product.getSpecifications()) {
                specs.put(spec.getSpecKey(), spec.getSpecValue());
            }
        } else if (product.getDetailedSpecsJson() != null) {
            try {
                Map<String, String> parsed = objectMapper.readValue(product.getDetailedSpecsJson(), new TypeReference<Map<String, String>>() {});
                specs.putAll(parsed);
            } catch (Exception ignored) {}
        }

        return ProductCardDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .brand(product.getBrand() != null ? product.getBrand().getName() : "")
                .category(product.getCategory() != null ? product.getCategory().getName() : "")
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .discountPercent(product.getDiscountPercent())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .stock(product.getStock())
                .primaryImageUrl(product.getPrimaryImageUrl())
                .tags(product.getTags())
                .specsSummary(specs)
                .build();
    }
}
