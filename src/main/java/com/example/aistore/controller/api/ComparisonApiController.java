package com.example.aistore.controller.api;

import com.example.aistore.dto.ProductComparisonDto;
import com.example.aistore.service.ProductComparisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compare")
public class ComparisonApiController {

    private final ProductComparisonService comparisonService;
    public ComparisonApiController(ProductComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }


    @GetMapping
    public ResponseEntity<ProductComparisonDto> compare(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        ProductComparisonDto result = comparisonService.compareProducts(idList);
        return ResponseEntity.ok(result);
    }
}
