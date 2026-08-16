package com.example.aistore.controller;

import com.example.aistore.dto.UserPreferenceDto;
import com.example.aistore.entity.Address;
import com.example.aistore.entity.Brand;
import com.example.aistore.entity.Category;
import com.example.aistore.entity.Order;
import com.example.aistore.entity.User;
import com.example.aistore.repository.BrandRepository;
import com.example.aistore.repository.CategoryRepository;
import com.example.aistore.service.OrderService;
import com.example.aistore.service.UserPreferenceService;
import com.example.aistore.service.UserService;
import com.example.aistore.service.email.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProfileController.class);

    private final UserService userService;
    private final UserPreferenceService userPreferenceService;
    private final OrderService orderService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final OtpService otpService;

    public ProfileController(UserService userService,
                             UserPreferenceService userPreferenceService,
                             OrderService orderService,
                             CategoryRepository categoryRepository,
                             BrandRepository brandRepository,
                             OtpService otpService) {
        this.userService = userService;
        this.userPreferenceService = userPreferenceService;
        this.orderService = orderService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.otpService = otpService;
    }

    @GetMapping
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam(value = "tab", defaultValue = "info") String activeTab,
                              Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Address> addresses = userService.getUserAddresses(user);
        UserPreferenceDto preference = userPreferenceService.getUserPreferenceDto(user.getId());
        List<Order> orders = orderService.getUserOrders(user);
        List<Category> allCategories = categoryRepository.findAll();
        List<Brand> allBrands = brandRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("preference", preference);
        model.addAttribute("orders", orders);
        model.addAttribute("categories", allCategories);
        model.addAttribute("brands", allBrands);
        model.addAttribute("activeTab", activeTab);

        return "user/profile";
    }

    @PostMapping("/update-info")
    public String updatePersonalInfo(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam("fullName") String fullName,
                                     @RequestParam("phone") String phone,
                                     RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.updatePersonalInfo(user, fullName, phone);
            redirectAttributes.addFlashAttribute("successMessage", "Personal information updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating personal info: " + e.getMessage());
        }
        return "redirect:/profile?tab=info";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New password and confirmation do not match.");
            return "redirect:/profile?tab=security";
        }

        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.changePassword(user, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile?tab=security";
    }

    @PostMapping("/address")
    public String addAddress(@AuthenticationPrincipal UserDetails userDetails,
                             Address address,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.addAddress(user, address);
            redirectAttributes.addFlashAttribute("successMessage", "Address added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add address: " + e.getMessage());
        }
        return "redirect:/profile?tab=addresses";
    }

    @PostMapping("/address/{id}/edit")
    public String editAddress(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("id") Long addressId,
                              Address address,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.updateAddress(user, addressId, address);
            redirectAttributes.addFlashAttribute("successMessage", "Address updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update address: " + e.getMessage());
        }
        return "redirect:/profile?tab=addresses";
    }

    @PostMapping("/address/{id}/delete")
    public String deleteAddress(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable("id") Long addressId,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.deleteAddress(user, addressId);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete address: " + e.getMessage());
        }
        return "redirect:/profile?tab=addresses";
    }

    @PostMapping("/address/{id}/default")
    public String setDefaultAddress(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable("id") Long addressId,
                                    RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.setDefaultAddress(user, addressId);
            redirectAttributes.addFlashAttribute("successMessage", "Default address updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to set default address: " + e.getMessage());
        }
        return "redirect:/profile?tab=addresses";
    }

    @PostMapping("/update-preferences")
    public String updateShoppingPreferences(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam(value = "minBudget", required = false) BigDecimal minBudget,
                                            @RequestParam(value = "maxBudget", required = false) BigDecimal maxBudget,
                                            @RequestParam(value = "preferredCategories", required = false) List<String> categories,
                                            @RequestParam(value = "preferredBrands", required = false) List<String> brands,
                                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userPreferenceService.updateExplicitPreferences(user.getId(), minBudget, maxBudget, categories, brands);
            redirectAttributes.addFlashAttribute("successMessage", "AI Shopping preferences updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update preferences: " + e.getMessage());
        }
        return "redirect:/profile?tab=preferences";
    }

    @PostMapping("/preferences")
    public String updatePrivacyPreferences(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam(defaultValue = "false") boolean recommendationsEnabled,
                                           @RequestParam(defaultValue = "false") boolean behaviorTrackingEnabled,
                                           @RequestParam(defaultValue = "false") boolean aiChatHistoryEnabled,
                                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userPreferenceService.updatePrivacySettings(user.getId(), recommendationsEnabled, behaviorTrackingEnabled, aiChatHistoryEnabled);
            redirectAttributes.addFlashAttribute("successMessage", "AI Privacy and Telemetry preferences updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update privacy settings: " + e.getMessage());
        }
        return "redirect:/profile?tab=preferences";
    }

    @PostMapping("/email/send-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> requestEmailChangeOtp(@AuthenticationPrincipal UserDetails userDetails,
                                                                     @RequestBody Map<String, String> payload) {
        String newEmail = payload.get("newEmail");
        if (newEmail == null || !newEmail.contains("@")) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "A valid new email address is required"));
        }

        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        String code = otpService.generateAndSendOtp(newEmail.trim(), user.getFullName(), "Email Update Verification");
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Verification code sent to " + newEmail + " via Brevo Email API.",
                "simulatedCode", code
        ));
    }

    @PostMapping("/email/verify-otp")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyEmailChangeOtp(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestBody Map<String, String> payload) {
        String newEmail = payload.get("newEmail");
        String otp = payload.get("otp");

        if (newEmail == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email and OTP are required"));
        }

        boolean verified = otpService.verifyOtp(newEmail.trim(), otp.trim());
        if (!verified) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid or expired OTP code"));
        }

        try {
            User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
            userService.updateEmail(user, newEmail.trim());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Email address verified and updated successfully! Please sign in with your new email.",
                    "redirectUrl", "/login?emailChanged=true"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
