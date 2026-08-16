package com.example.aistore.service.email;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OtpService.class);

    private final BrevoEmailService brevoEmailService;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    public OtpService(BrevoEmailService brevoEmailService) {
        this.brevoEmailService = brevoEmailService;
    }

    public static class OtpEntry {
        private final String code;
        private final Instant expiresAt;
        private final String purpose;
        private int attempts;

        public OtpEntry(String code, Instant expiresAt, String purpose) {
            this.code = code;
            this.expiresAt = expiresAt;
            this.purpose = purpose;
            this.attempts = 0;
        }

        public String getCode() { return code; }
        public Instant getExpiresAt() { return expiresAt; }
        public String getPurpose() { return purpose; }
        public int getAttempts() { return attempts; }
        public void incrementAttempts() { this.attempts++; }
        public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
    }

    /**
     * Generates a secure 6-digit OTP, stores it with a 5-minute TTL, and sends it via Brevo Email API.
     */
    public String generateAndSendOtp(String email, String fullName, String purpose) {
        String code = String.format("%06d", random.nextInt(1000000));
        Instant expiresAt = Instant.now().plusSeconds(300); // 5 minutes validity

        otpStore.put(email.toLowerCase().trim(), new OtpEntry(code, expiresAt, purpose));

        log.info("Generated OTP [{}] for email: {} (Purpose: {})", code, email, purpose);

        // Send via Brevo Email Service
        boolean emailSent = brevoEmailService.sendOtpEmail(email, fullName, code, purpose);
        if (!emailSent) {
            log.info("Brevo email API dispatched with simulated fallback. OTP for [{}] is [{}]", email, code);
        }

        return code;
    }

    /**
     * Verifies if the provided OTP code matches the active code for the email.
     */
    public boolean verifyOtp(String email, String inputCode) {
        if (email == null || inputCode == null) return false;
        String key = email.toLowerCase().trim();
        OtpEntry entry = otpStore.get(key);

        if (entry == null) {
            log.warn("No active OTP entry found for email: {}", email);
            return false;
        }

        if (entry.isExpired()) {
            log.warn("OTP for email {} has expired.", email);
            otpStore.remove(key);
            return false;
        }

        entry.incrementAttempts();
        if (entry.getAttempts() > 5) {
            log.warn("Exceeded maximum OTP verification attempts for email: {}", email);
            otpStore.remove(key);
            return false;
        }

        if (entry.getCode().equals(inputCode.trim())) {
            log.info("OTP successfully verified for email: {}", email);
            otpStore.remove(key);
            return true;
        }

        log.warn("Invalid OTP entered for email: {}. Entered: {}", email, inputCode);
        return false;
    }

    public OtpEntry getActiveOtp(String email) {
        if (email == null) return null;
        return otpStore.get(email.toLowerCase().trim());
    }
}
