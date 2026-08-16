package com.example.aistore.dto;

import java.util.Map;

public class ToolCallDto {
    private String toolName;
    private Map<String, Object> arguments;
    private String reasoning;

    public ToolCallDto() {}

    public ToolCallDto(String toolName, Map<String, Object> arguments, String reasoning) {
        this.toolName = toolName;
        this.arguments = arguments;
        this.reasoning = reasoning;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String toolName;
        private Map<String, Object> arguments;
        private String reasoning;

        public Builder toolName(String toolName) { this.toolName = toolName; return this; }
        public Builder arguments(Map<String, Object> arguments) { this.arguments = arguments; return this; }
        public Builder reasoning(String reasoning) { this.reasoning = reasoning; return this; }

        public ToolCallDto build() {
            return new ToolCallDto(toolName, arguments, reasoning);
        }
    }

    public String getToolName() { return toolName; }
    public void setToolName(String toolName) { this.toolName = toolName; }

    public Map<String, Object> getArguments() { return arguments; }
    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
}
