package com.example.aistore.controller;

import com.example.aistore.entity.User;
import com.example.aistore.repository.UserRepository;
import com.example.aistore.service.email.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class OtpController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OtpController.class);

    private final OtpService otpService;
    private final UserRepository userRepository;

    public OtpController(OtpService otpService, UserRepository userRepository) {
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify-otp")
    public String verifyOtpPage(@RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "purpose", defaultValue = "Account Verification") String purpose,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {
        if (email == null || email.trim().isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute("email", email.trim());
        model.addAttribute("purpose", purpose);
        
        OtpService.OtpEntry activeOtp = otpService.getActiveOtp(email.trim());
        if (activeOtp != null) {
            model.addAttribute("simulatedOtp", activeOtp.getCode());
        }

        if (error != null) {
            model.addAttribute("errorMessage", "The verification code entered is invalid or has expired. Please try again.");
        }

        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtpForm(@RequestParam("email") String email,
                                      @RequestParam("otp") String otpCode,
                                      @RequestParam(value = "purpose", defaultValue = "Account Verification") String purpose,
                                      Model model) {
        boolean verified = otpService.verifyOtp(email, otpCode);
        if (!verified) {
            return "redirect:/verify-otp?email=" + email + "&purpose=" + purpose + "&error=invalid";
        }

        // Activate user
        User user = userRepository.findByEmail(email.toLowerCase().trim()).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        }

        return "redirect:/login?verified=true";
    }

    @PostMapping("/api/otp/send")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendOtpApi(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String purpose = payload.getOrDefault("purpose", "Account Verification");
        String name = payload.getOrDefault("name", "Customer");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email is required"));
        }

        String code = otpService.generateAndSendOtp(email.trim(), name, purpose);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Verification code dispatched via Brevo Email API.",
                "email", email,
                "simulatedCode", code
        ));
    }

    @PostMapping("/api/otp/verify")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyOtpApi(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("otp");

        if (email == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email and OTP code are required"));
        }

        boolean verified = otpService.verifyOtp(email, code);
        if (verified) {
            User user = userRepository.findByEmail(email.toLowerCase().trim()).orElse(null);
            if (user != null) {
                user.setActive(true);
                userRepository.save(user);
            }
            return ResponseEntity.ok(Map.of("success", true, "message", "OTP verified successfully!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid or expired OTP code."));
        }
    }
}
