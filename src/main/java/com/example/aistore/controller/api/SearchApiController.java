package com.example.aistore.controller.api;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.User;
import com.example.aistore.service.SearchIntelligenceService;
import com.example.aistore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchApiController {

    private final SearchIntelligenceService searchService;
    private final UserService userService;
    public SearchApiController(SearchIntelligenceService searchService, UserService userService) {
        this.searchService = searchService;
        this.userService = userService;
    }


    @GetMapping("/natural")
    public ResponseEntity<List<ProductCardDto>> naturalSearch(
            @RequestParam String q,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) userId = user.getId();
        }

        List<ProductCardDto> results = searchService.processNaturalLanguageSearch(userId, session.getId(), q);
        return ResponseEntity.ok(results);
    }
}
