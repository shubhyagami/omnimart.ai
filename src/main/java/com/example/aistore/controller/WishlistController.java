package com.example.aistore.controller;

import com.example.aistore.dto.ProductCardDto;
import com.example.aistore.entity.User;
import com.example.aistore.service.UserService;
import com.example.aistore.service.WishlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    public WishlistController(WishlistService wishlistService, UserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }


    @GetMapping
    public String viewWishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<ProductCardDto> wishlistProducts = wishlistService.getUserWishlistProducts(user);
        model.addAttribute("products", wishlistProducts);
        return "user/wishlist";
    }

    @PostMapping("/toggle")
    public String toggleWishlist(@RequestParam Long productId,
                                 @RequestParam(defaultValue = "") String redirectUrl,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        wishlistService.toggleWishlist(user, productId);

        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            return "redirect:" + redirectUrl;
        }
        return "redirect:/wishlist";
    }
}
