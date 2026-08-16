package com.example.aistore.controller.api;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.User;
import com.example.aistore.service.HybridRecommendationService;
import com.example.aistore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationApiController {

    private final HybridRecommendationService recommendationService;
    private final UserService userService;
    public RecommendationApiController(HybridRecommendationService recommendationService, UserService userService) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<ProductCardDto>> getRecommendations(
            @RequestParam(defaultValue = "8") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) userId = user.getId();
        }

        List<ProductCardDto> list = recommendationService.getPersonalizedRecommendations(userId, limit);
        return ResponseEntity.ok(list);
    }
}
