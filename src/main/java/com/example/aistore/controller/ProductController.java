package com.example.aistore.controller;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.dto.ProductFilterDto;
import com.example.aistore.entity.Category;
import com.example.aistore.entity.MarketProduct;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.Review;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import com.example.aistore.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ReviewService reviewService;
    private final WishlistService wishlistService;
    private final MarketIntelligenceService marketService;
    private final UserInteractionService interactionService;
    private final UserService userService;
    private final com.example.aistore.ai.MockAIProvider mockAIProvider;
    public ProductController(ProductService productService, CategoryService categoryService, BrandService brandService, ReviewService reviewService, WishlistService wishlistService, MarketIntelligenceService marketService, UserInteractionService interactionService, UserService userService, com.example.aistore.ai.MockAIProvider mockAIProvider) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.reviewService = reviewService;
        this.wishlistService = wishlistService;
        this.marketService = marketService;
        this.interactionService = interactionService;
        this.userService = userService;
        this.mockAIProvider = mockAIProvider;
    }

    @GetMapping("/products")
    public String listProducts(@ModelAttribute ProductFilterDto filter, Model model) {
        if (filter.getQuery() != null && !filter.getQuery().trim().isEmpty()) {
            String q = filter.getQuery().trim();
            // Check if query is natural language sentence
            if (q.contains("under") || q.contains("below") || q.contains("best") || q.contains("phone") || q.contains("laptop") || q.contains("₹") || q.contains("with") || q.contains("rating")) {
                java.util.Map<String, Object> parsed = mockAIProvider.parseNaturalLanguageSearch(q);
                if (parsed.containsKey("category") && filter.getCategoryId() == null) {
                    categoryService.getAllCategories().stream()
                            .filter(c -> c.getName().equalsIgnoreCase((String) parsed.get("category")))
                            .findFirst()
                            .ifPresent(c -> filter.setCategoryId(c.getId()));
                }
                if (parsed.containsKey("maxPrice") && filter.getMaxPrice() == null) {
                    filter.setMaxPrice((java.math.BigDecimal) parsed.get("maxPrice"));
                }
                if (parsed.containsKey("minPrice") && filter.getMinPrice() == null) {
                    filter.setMinPrice((java.math.BigDecimal) parsed.get("minPrice"));
                }
                if (parsed.containsKey("minRating") && filter.getMinRating() == null) {
                    filter.setMinRating((Double) parsed.get("minRating"));
                }
                if (parsed.containsKey("query")) {
                    filter.setQuery((String) parsed.get("query"));
                } else {
                    filter.setQuery(null); // clear residual raw text so category & price filter apply cleanly
                }
            }
        }

        Page<ProductCardDto> page = productService.filterProducts(filter);
        model.addAttribute("page", page);
        model.addAttribute("products", page.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "products/list";
    }

    @GetMapping("/category/{slug}")
    public String viewCategory(@PathVariable String slug, @ModelAttribute ProductFilterDto filter, Model model) {
        Category category = categoryService.getCategoryBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + slug));

        filter.setCategoryId(category.getId());
        Page<ProductCardDto> page = productService.filterProducts(filter);

        model.addAttribute("category", category);
        model.addAttribute("page", page);
        model.addAttribute("products", page.getContent());
        model.addAttribute("filter", filter);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        User user = null;
        if (userDetails != null) {
            user = userService.findByEmail(userDetails.getUsername()).orElse(null);
        }

        // Record behavioral telemetry for PRODUCT_VIEW
        Long userId = user != null ? user.getId() : null;
        interactionService.recordInteraction(userId, null, UserInteraction.InteractionType.PRODUCT_VIEW, product.getId(), 20, null);

        List<ProductCardDto> relatedProducts = productService.getRelatedProducts(product, 4);
        List<Review> reviews = reviewService.getProductReviews(product);
        List<MarketProduct> marketBenchmarks = marketService.getCompetitorPricesForProduct(product);
        boolean inWishlist = wishlistService.isInWishlist(user, product.getId());

        model.addAttribute("product", product);
        model.addAttribute("card", productService.toCardDto(product));
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("reviews", reviews);
        model.addAttribute("marketBenchmarks", marketBenchmarks);
        model.addAttribute("inWishlist", inWishlist);

        return "products/detail";
    }

    @PostMapping("/products/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @RequestParam int rating,
                            @RequestParam String title,
                            @RequestParam String comment,
                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        reviewService.addReview(user, id, rating, title, comment);

        return "redirect:/products/" + id + "?reviewAdded=true";
    }
}
