package com.example.aistore.controller;

import com.example.aistore.dto.CartDto;
import com.example.aistore.entity.User;
import com.example.aistore.service.CartService;
import com.example.aistore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping
    public String viewCart(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Long userId = getUserId(userDetails);
        CartDto cart = cartService.getCartDto(userId, session.getId());
        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        cartService.addToCart(userId, session.getId(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId,
                                 @RequestParam int quantity,
                                 HttpSession session,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        cartService.updateQuantity(userId, session.getId(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 HttpSession session,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        cartService.removeFromCart(userId, session.getId(), productId);
        return "redirect:/cart";
    }

    private Long getUserId(UserDetails userDetails) {
        if (userDetails == null) return null;
        return userService.findByEmail(userDetails.getUsername()).map(User::getId).orElse(null);
    }
}
