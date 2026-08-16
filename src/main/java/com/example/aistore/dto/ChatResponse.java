package com.example.aistore.dto;

import java.util.List;

public class ChatResponse {
    private String conversationId;
    private String message;
    private List<ProductCardDto> products;
    private String reasoningSummary;
    private List<String> suggestedFollowUps;
    private String toolUsed;
    private String provider;

    public ChatResponse() {}

    public ChatResponse(String conversationId, String message, List<ProductCardDto> products, String reasoningSummary, List<String> suggestedFollowUps, String toolUsed, String provider) {
        this.conversationId = conversationId;
        this.message = message;
        this.products = products;
        this.reasoningSummary = reasoningSummary;
        this.suggestedFollowUps = suggestedFollowUps;
        this.toolUsed = toolUsed;
        this.provider = provider;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String conversationId;
        private String message;
        private List<ProductCardDto> products;
        private String reasoningSummary;
        private List<String> suggestedFollowUps;
        private String toolUsed;
        private String provider;

        public Builder conversationId(String conversationId) { this.conversationId = conversationId; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder products(List<ProductCardDto> products) { this.products = products; return this; }
        public Builder reasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; return this; }
        public Builder suggestedFollowUps(List<String> suggestedFollowUps) { this.suggestedFollowUps = suggestedFollowUps; return this; }
        public Builder toolUsed(String toolUsed) { this.toolUsed = toolUsed; return this; }
        public Builder provider(String provider) { this.provider = provider; return this; }

        public ChatResponse build() {
            return new ChatResponse(conversationId, message, products, reasoningSummary, suggestedFollowUps, toolUsed, provider);
        }
    }

    public String getConversationId() { return conversationId; }
    public void setConversationId(String conversationId) { this.conversationId = conversationId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<ProductCardDto> getProducts() { return products; }
    public void setProducts(List<ProductCardDto> products) { this.products = products; }

    public String getReasoningSummary() { return reasoningSummary; }
    public void setReasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; }

    public List<String> getSuggestedFollowUps() { return suggestedFollowUps; }
    public void setSuggestedFollowUps(List<String> suggestedFollowUps) { this.suggestedFollowUps = suggestedFollowUps; }

    public String getToolUsed() { return toolUsed; }
    public void setToolUsed(String toolUsed) { this.toolUsed = toolUsed; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
