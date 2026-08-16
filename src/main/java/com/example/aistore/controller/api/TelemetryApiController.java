package com.example.aistore.controller.api;

import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import com.example.aistore.service.UserInteractionService;
import com.example.aistore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryApiController {

    private final UserInteractionService interactionService;
    private final UserService userService;
    public TelemetryApiController(UserInteractionService interactionService, UserService userService) {
        this.interactionService = interactionService;
        this.userService = userService;
    }


    @PostMapping("/interaction")
    public ResponseEntity<Map<String, String>> recordInteraction(@RequestBody InteractionPayload payload,
                                                                 HttpSession session,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) userId = user.getId();
        }

        UserInteraction.InteractionType type;
        try {
            type = UserInteraction.InteractionType.valueOf(payload.getEventType());
        } catch (Exception e) {
            type = UserInteraction.InteractionType.PRODUCT_VIEW;
        }

        interactionService.recordInteraction(userId, session.getId(), type, payload.getProductId(), payload.getDwellTimeSeconds(), payload.getMetadata());
        return ResponseEntity.ok(Map.of("status", "recorded"));
    }

    public static class InteractionPayload {
        private String eventType;
        private Long productId;
        private int dwellTimeSeconds;
        private String metadata;

        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getDwellTimeSeconds() { return dwellTimeSeconds; }
        public void setDwellTimeSeconds(int dwellTimeSeconds) { this.dwellTimeSeconds = dwellTimeSeconds; }

        public String getMetadata() { return metadata; }
        public void setMetadata(String metadata) { this.metadata = metadata; }
    }
}
