package com.example.aistore.controller.api;

import com.example.aistore.ai.AIOrchestrator;
import com.example.aistore.dto.ChatRequest;
import com.example.aistore.dto.ChatResponse;
import com.example.aistore.entity.User;
import com.example.aistore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatApiController {

    private final AIOrchestrator aiOrchestrator;
    private final UserService userService;
    public ChatApiController(AIOrchestrator aiOrchestrator, UserService userService) {
        this.aiOrchestrator = aiOrchestrator;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request,
                                             HttpSession session,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = null;
        if (userDetails != null) {
            User user = userService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) userId = user.getId();
        }

        ChatResponse response = aiOrchestrator.processChat(userId, session.getId(), request);
        return ResponseEntity.ok(response);
    }
}
