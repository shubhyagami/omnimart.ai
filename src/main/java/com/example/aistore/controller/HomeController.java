package com.example.aistore.controller;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.User;
import com.example.aistore.service.CategoryService;
import com.example.aistore.service.HybridRecommendationService;
import com.example.aistore.service.ProductService;
import com.example.aistore.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final HybridRecommendationService recommendationService;
    private final UserService userService;
    public HomeController(CategoryService categoryService, ProductService productService, HybridRecommendationService recommendationService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.recommendationService = recommendationService;
        this.userService = userService;
    }


    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) userId = user.getId();
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("featuredProducts", productService.getFeaturedProducts());
        model.addAttribute("trendingProducts", productService.getTrendingProducts());
        model.addAttribute("topRatedProducts", productService.getTopRatedProducts());
        model.addAttribute("newArrivals", productService.getNewArrivals());
        model.addAttribute("recommendedProducts", recommendationService.getPersonalizedRecommendations(userId, 8));

        return "index";
    }
}
