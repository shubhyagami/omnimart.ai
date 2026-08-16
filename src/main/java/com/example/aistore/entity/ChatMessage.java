package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private ChatConversation conversation;

    @Column(nullable = false)
    private String sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String toolCallsJson;

    @Column(columnDefinition = "TEXT")
    private String recommendedProductIdsJson;

    @Column(columnDefinition = "TEXT")
    private String reasoningSummary;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ChatMessage() {}

    public ChatMessage(Long id, ChatConversation conversation, String sender, String content, String toolCallsJson, String recommendedProductIdsJson, String reasoningSummary, LocalDateTime createdAt) {
        this.id = id;
        this.conversation = conversation;
        this.sender = sender;
        this.content = content;
        this.toolCallsJson = toolCallsJson;
        this.recommendedProductIdsJson = recommendedProductIdsJson;
        this.reasoningSummary = reasoningSummary;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private ChatConversation conversation;
        private String sender;
        private String content;
        private String toolCallsJson;
        private String recommendedProductIdsJson;
        private String reasoningSummary;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder conversation(ChatConversation conversation) { this.conversation = conversation; return this; }
        public Builder sender(String sender) { this.sender = sender; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder toolCallsJson(String toolCallsJson) { this.toolCallsJson = toolCallsJson; return this; }
        public Builder recommendedProductIdsJson(String recommendedProductIdsJson) { this.recommendedProductIdsJson = recommendedProductIdsJson; return this; }
        public Builder reasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public ChatMessage build() {
            return new ChatMessage(id, conversation, sender, content, toolCallsJson, recommendedProductIdsJson, reasoningSummary, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChatConversation getConversation() { return conversation; }
    public void setConversation(ChatConversation conversation) { this.conversation = conversation; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getToolCallsJson() { return toolCallsJson; }
    public void setToolCallsJson(String toolCallsJson) { this.toolCallsJson = toolCallsJson; }

    public String getRecommendedProductIdsJson() { return recommendedProductIdsJson; }
    public void setRecommendedProductIdsJson(String recommendedProductIdsJson) { this.recommendedProductIdsJson = recommendedProductIdsJson; }

    public String getReasoningSummary() { return reasoningSummary; }
    public void setReasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
