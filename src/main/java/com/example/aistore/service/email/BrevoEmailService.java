package com.example.aistore.service.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@Service
public class BrevoEmailService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BrevoEmailService.class);

    private final String apiKey;
    private final String senderEmail;
    private final String senderName;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public BrevoEmailService(
            @Value("${brevo.api-key:}") String apiKey,
            @Value("${brevo.sender.email:support@omnimart-ai.com}") String senderEmail,
            @Value("${brevo.sender.name:OmniMart AI}") String senderName,
            ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .defaultHeader("api-key", this.apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Sends an OTP verification email using Brevo's Transactional Email API.
     */
    public boolean sendOtpEmail(String recipientEmail, String recipientName, String otpCode, String purpose) {
        String subject = "🔒 Your OmniMart AI Verification Code: " + otpCode;
        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>OmniMart AI Verification</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #030712; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;">
              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #030712; padding: 30px 10px;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="100%%" style="max-width: 580px; background-color: #0f172a; border-radius: 12px; border: 1px solid #334155; overflow: hidden; box-shadow: 0 15px 35px rgba(0,0,0,0.6);" cellspacing="0" cellpadding="0" border="0">
                      <!-- Brand Header -->
                      <tr>
                        <td style="background: linear-gradient(135deg, #0b1120 0%%, #1e293b 100%%); padding: 26px 20px; text-align: center; border-bottom: 2px solid #f59e0b;">
                          <span style="font-size: 26px; font-weight: 900; color: #ffffff; letter-spacing: 1px;">OMNI<span style="color: #f59e0b;">MART</span> AI</span>
                        </td>
                      </tr>
                      <!-- Main Body -->
                      <tr>
                        <td style="padding: 35px 30px;">
                          <div style="text-align: center; margin-bottom: 15px;">
                            <span style="display: inline-block; background-color: rgba(245, 158, 11, 0.2); color: #fbbf24; border: 1px solid #f59e0b; padding: 5px 16px; border-radius: 20px; font-size: 12px; font-weight: 700; text-transform: uppercase;">%s</span>
                          </div>
                          <h2 style="color: #ffffff; font-size: 22px; font-weight: 800; text-align: center; margin: 0 0 18px 0;">Security Verification Code</h2>
                          <p style="color: #e2e8f0; font-size: 15px; line-height: 1.6; margin: 0 0 14px 0;">Hello <strong style="color: #ffffff;">%s</strong>,</p>
                          <p style="color: #cbd5e1; font-size: 14px; line-height: 1.6; margin: 0 0 20px 0;">Please enter the following 6-digit One-Time Password (OTP) to complete your verification on <strong>OmniMart AI</strong>. This code is active for <strong>5 minutes</strong>.</p>
                          
                          <!-- OTP Box -->
                          <div style="background-color: #1e293b; border: 2px dashed #f59e0b; border-radius: 10px; padding: 22px; text-align: center; margin: 25px 0;">
                            <div style="font-size: 38px; font-weight: 900; letter-spacing: 10px; color: #f59e0b; font-family: Consolas, Monaco, monospace;">%s</div>
                            <span style="display: block; color: #94a3b8; font-size: 12px; margin-top: 8px;">Single-Use Security Passcode</span>
                          </div>
                          
                          <p style="color: #94a3b8; font-size: 13px; line-height: 1.5; margin: 20px 0 0 0; padding-top: 15px; border-top: 1px solid #334155;">
                            ⚠️ If you did not make this request, please ignore this email. Never share your OTP code with anyone.
                          </p>
                        </td>
                      </tr>
                      <!-- Footer -->
                      <tr>
                        <td style="background-color: #0b1120; border-top: 1px solid #1e293b; padding: 20px; text-align: center; color: #94a3b8; font-size: 12px;">
                          <div style="margin-bottom: 6px; color: #cbd5e1;">Design created by <strong>Shubh Kumar</strong></div>
                          &copy; 2026 OmniMart AI Technologies Inc. Powered by Brevo Email API.
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """, purpose, (recipientName != null ? recipientName : "Valued Customer"), otpCode);

        return sendTransactionalEmail(recipientEmail, recipientName, subject, htmlContent);
    }

    /**
     * Sends an Order Confirmation & Tracking email via Brevo.
     */
    public boolean sendOrderConfirmationEmail(String recipientEmail, String recipientName, String orderNumber, BigDecimal totalAmount, String itemsSummary) {
        String subject = "📦 OmniMart AI Order Confirmed: " + orderNumber;
        String formattedPrice = totalAmount != null ? String.format("%,.2f", totalAmount) : "0.00";
        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Order Confirmation</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #030712; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;">
              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0" style="background-color: #030712; padding: 30px 10px;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="100%%" style="max-width: 600px; background-color: #0f172a; border-radius: 12px; border: 1px solid #334155; overflow: hidden; box-shadow: 0 15px 35px rgba(0,0,0,0.6);" cellspacing="0" cellpadding="0" border="0">
                      <!-- Brand Header -->
                      <tr>
                        <td style="background: linear-gradient(135deg, #0b1120 0%%, #1e293b 100%%); padding: 26px 20px; text-align: center; border-bottom: 2px solid #10b981;">
                          <span style="font-size: 26px; font-weight: 900; color: #ffffff; letter-spacing: 1px;">OMNI<span style="color: #f59e0b;">MART</span> AI</span>
                        </td>
                      </tr>
                      <!-- Main Body -->
                      <tr>
                        <td style="padding: 35px 30px;">
                          <h2 style="color: #34d399; font-size: 24px; font-weight: 800; margin: 0 0 14px 0;">✓ Order Confirmed!</h2>
                          <p style="color: #f1f5f9; font-size: 15px; line-height: 1.6; margin: 0 0 18px 0;">
                            Hi <strong style="color: #ffffff;">%s</strong>, thank you for shopping with OmniMart AI! We have received your order and our automated fulfillment network is processing your shipment.
                          </p>
                          
                          <!-- Order Details Card -->
                          <div style="background-color: #1e293b; border-radius: 10px; padding: 22px; margin: 24px 0; border: 1px solid #475569;">
                            <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" border="0">
                              <tr>
                                <td style="padding: 6px 0; color: #94a3b8; font-size: 14px; width: 35%%;"><strong>Order Number:</strong></td>
                                <td style="padding: 6px 0; color: #f59e0b; font-size: 15px; font-weight: 800; font-family: Consolas, Monaco, monospace;">%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 6px 0; color: #94a3b8; font-size: 14px;"><strong>Total Amount:</strong></td>
                                <td style="padding: 6px 0; color: #34d399; font-size: 17px; font-weight: 800;">₹%s</td>
                              </tr>
                              <tr>
                                <td style="padding: 6px 0; color: #94a3b8; font-size: 14px; vertical-align: top;"><strong>Items Summary:</strong></td>
                                <td style="padding: 6px 0; color: #ffffff; font-size: 14px; font-weight: 600; line-height: 1.5;">%s</td>
                              </tr>
                            </table>
                          </div>
                          
                          <p style="color: #cbd5e1; font-size: 14px; line-height: 1.5; margin: 20px 0 25px 0;">
                            You can track your live shipment milestone progress, carrier ETA, and invoices directly in your OmniMart portal.
                          </p>

                          <div style="text-align: center; margin: 25px 0 10px 0;">
                            <a href="http://localhost:8080/orders" style="display: inline-block; background-color: #f59e0b; color: #0f172a; font-weight: 800; font-size: 14px; padding: 12px 28px; border-radius: 8px; text-decoration: none; box-shadow: 0 4px 12px rgba(245,158,11,0.3);">
                              Track Your Order Live →
                            </a>
                          </div>
                        </td>
                      </tr>
                      <!-- Footer -->
                      <tr>
                        <td style="background-color: #0b1120; border-top: 1px solid #1e293b; padding: 20px; text-align: center; color: #94a3b8; font-size: 12px;">
                          <div style="margin-bottom: 6px; color: #cbd5e1;">Design created by <strong>Shubh Kumar</strong></div>
                          &copy; 2026 OmniMart AI Technologies Inc. All rights reserved. Powered by Brevo Email API.
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """, (recipientName != null ? recipientName : "Valued Customer"), orderNumber, formattedPrice, itemsSummary);

        return sendTransactionalEmail(recipientEmail, recipientName, subject, htmlContent);
    }

    /**
     * Executes the HTTP request to Brevo Transactional Email endpoint.
     */
    public boolean sendTransactionalEmail(String toEmail, String toName, String subject, String htmlContent) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", Map.of("name", senderName, "email", senderEmail));
            payload.put("to", List.of(Map.of("email", toEmail, "name", toName != null ? toName : toEmail)));
            payload.put("subject", subject);
            payload.put("htmlContent", htmlContent);

            log.info("Sending transactional email via Brevo API to: {} | Subject: '{}'", toEmail, subject);

            String response = webClient.post()
                    .uri("/smtp/email")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            log.info("Brevo API Email dispatched successfully. Response: {}", response);
            return true;
        } catch (Exception e) {
            log.warn("Brevo API call was not processed by remote server: {}. (If IP whitelisting is active in Brevo, authorize the IP at https://app.brevo.com/security/authorised_ips).", e.getMessage());
            return false;
        }
    }
}
