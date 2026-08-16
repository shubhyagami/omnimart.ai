package com.example.aistore.controller;

import com.example.aistore.dto.CartDto;
import com.example.aistore.dto.CheckoutRequestDto;
import com.example.aistore.entity.Address;
import com.example.aistore.entity.Order;
import com.example.aistore.entity.User;
import com.example.aistore.service.CartService;
import com.example.aistore.service.OrderService;
import com.example.aistore.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        CartDto cart = cartService.getCartDto(user.getId(), session.getId());

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        List<Address> addresses = userService.getUserAddresses(user);
        CheckoutRequestDto form = new CheckoutRequestDto();
        if (!addresses.isEmpty()) {
            Address def = addresses.stream().filter(Address::isDefault).findFirst().orElse(addresses.get(0));
            form.setAddressId(def.getId());
            form.setFullName(def.getFullName());
            form.setStreetAddress(def.getStreetAddress());
            form.setApartment(def.getApartment());
            form.setCity(def.getCity());
            form.setState(def.getState());
            form.setPostalCode(def.getPostalCode());
            form.setCountry(def.getCountry());
            form.setPhone(def.getPhone());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("addresses", addresses);
        model.addAttribute("checkoutRequest", form);

        return "checkout/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute("checkoutRequest") CheckoutRequestDto checkoutRequest,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();

        if (bindingResult.hasErrors()) {
            CartDto cart = cartService.getCartDto(user.getId(), session.getId());
            model.addAttribute("cart", cart);
            model.addAttribute("addresses", userService.getUserAddresses(user));
            return "checkout/checkout";
        }

        try {
            Order order = orderService.createOrderFromCart(user, checkoutRequest);
            return "redirect:/orders/" + order.getId() + "?success=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            CartDto cart = cartService.getCartDto(user.getId(), session.getId());
            model.addAttribute("cart", cart);
            model.addAttribute("addresses", userService.getUserAddresses(user));
            return "checkout/checkout";
        }
    }

    @GetMapping("/orders")
    public String orderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              @RequestParam(value = "success", required = false) Boolean success,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Order order = orderService.getOrderByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        model.addAttribute("order", order);
        model.addAttribute("isNewOrder", success != null && success);
        return "orders/detail";
    }

    @GetMapping("/orders/{id}/tracking")
    public String trackOrder(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Order order = orderService.getOrderByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));

        model.addAttribute("order", order);
        return "orders/tracking";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              @RequestParam String reason,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        orderService.cancelOrder(id, user, reason);
        return "redirect:/orders/" + id + "?cancelled=true";
    }

    @PostMapping("/orders/{id}/return")
    public String requestReturn(@PathVariable Long id,
                                @RequestParam String returnReason,
                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        orderService.requestReturn(id, user, returnReason);
        return "redirect:/orders/" + id + "?returnRequested=true";
    }
}
