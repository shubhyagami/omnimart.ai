package com.example.aistore.controller;

import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.service.CategoryService;
import com.example.aistore.service.ProductComparisonService;
import com.example.aistore.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CompareController {

    private final ProductComparisonService comparisonService;
    private final ProductService productService;
    private final CategoryService categoryService;
    public CompareController(ProductComparisonService comparisonService, ProductService productService, CategoryService categoryService) {
        this.comparisonService = comparisonService;
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/compare")
    public String comparePage(@RequestParam(value = "ids", required = false) String idsParam, Model model) {
        List<Long> ids = Collections.emptyList();
        if (idsParam != null && !idsParam.trim().isEmpty()) {
            try {
                ids = Arrays.stream(idsParam.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        ProductComparisonDto comparison = comparisonService.compareProducts(ids);
        model.addAttribute("comparison", comparison);
        model.addAttribute("allProducts", productService.getTopRatedProducts());
        model.addAttribute("categories", categoryService.getAllCategories());

        return "compare/compare";
    }
}
