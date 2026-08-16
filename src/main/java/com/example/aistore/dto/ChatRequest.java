package com.example.aistore.dto;

import java.util.Map;

public class ChatRequest {
    private String conversationId;
    private String message;
    private Long currentProductId;
    private Long currentCategoryId;
    private Map<String, Object> context;

    public ChatRequest() {}

    public ChatRequest(String conversationId, String message, Long currentProductId, Long currentCategoryId, Map<String, Object> context) {
        this.conversationId = conversationId;
        this.message = message;
        this.currentProductId = currentProductId;
        this.currentCategoryId = currentCategoryId;
        this.context = context;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String conversationId;
        private String message;
        private Long currentProductId;
        private Long currentCategoryId;
        private Map<String, Object> context;

        public Builder conversationId(String conversationId) { this.conversationId = conversationId; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder currentProductId(Long currentProductId) { this.currentProductId = currentProductId; return this; }
        public Builder currentCategoryId(Long currentCategoryId) { this.currentCategoryId = currentCategoryId; return this; }
        public Builder context(Map<String, Object> context) { this.context = context; return this; }

        public ChatRequest build() {
            return new ChatRequest(conversationId, message, currentProductId, currentCategoryId, context);
        }
    }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getCurrentProductId() { return currentProductId; }
    public void setCurrentProductId(Long currentProductId) { this.currentProductId = currentProductId; }

    public Long getCurrentCategoryId() { return currentCategoryId; }
    public void setCurrentCategoryId(Long currentCategoryId) { this.currentCategoryId = currentCategoryId; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
