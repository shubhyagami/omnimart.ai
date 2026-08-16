package com.example.aistore.controller;

import com.example.aistore.dto.AuthDtos;
import com.example.aistore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;
    private final com.example.aistore.service.email.OtpService otpService;

    public AuthController(UserService userService, com.example.aistore.service.email.OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "verified", required = false) String verified,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        if (verified != null) {
            model.addAttribute("successMessage", "Email verified successfully! You can now log in to your account.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration complete. Please log in.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new AuthDtos.RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") AuthDtos.RegisterRequest request,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(request);
            // Dispatch OTP email via Brevo
            otpService.generateAndSendOtp(request.getEmail(), request.getFullName(), "Account Registration");
            return "redirect:/verify-otp?email=" + request.getEmail() + "&purpose=Account+Registration";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
