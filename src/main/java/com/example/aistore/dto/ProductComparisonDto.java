package com.example.aistore.dto;

import java.util.List;
import java.util.Map;

public class ProductComparisonDto {
    private List<ProductCardDto> products;
    private List<String> specGroups;
    private Map<String, Map<Long, String>> specMatrix;
    private Long bestOverallId;
    private Long bestValueId;
    private Long bestCameraId;
    private Long bestPerformanceId;
    private Long bestBatteryId;
    private String aiSummary;
    private String bestOverallExplanation;
    private String bestValueExplanation;
    private String bestCameraExplanation;
    private String bestPerformanceExplanation;
    private String bestBatteryExplanation;

    public ProductComparisonDto() {}

    public ProductComparisonDto(List<ProductCardDto> products, List<String> specGroups, Map<String, Map<Long, String>> specMatrix, Long bestOverallId, Long bestValueId, Long bestCameraId, Long bestPerformanceId, Long bestBatteryId, String aiSummary, String bestOverallExplanation, String bestValueExplanation, String bestCameraExplanation, String bestPerformanceExplanation, String bestBatteryExplanation) {
        this.products = products;
        this.specGroups = specGroups;
        this.specMatrix = specMatrix;
        this.bestOverallId = bestOverallId;
        this.bestValueId = bestValueId;
        this.bestCameraId = bestCameraId;
        this.bestPerformanceId = bestPerformanceId;
        this.bestBatteryId = bestBatteryId;
        this.aiSummary = aiSummary;
        this.bestOverallExplanation = bestOverallExplanation;
        this.bestValueExplanation = bestValueExplanation;
        this.bestCameraExplanation = bestCameraExplanation;
        this.bestPerformanceExplanation = bestPerformanceExplanation;
        this.bestBatteryExplanation = bestBatteryExplanation;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private List<ProductCardDto> products;
        private List<String> specGroups;
        private Map<String, Map<Long, String>> specMatrix;
        private Long bestOverallId;
        private Long bestValueId;
        private Long bestCameraId;
        private Long bestPerformanceId;
        private Long bestBatteryId;
        private String aiSummary;
        private String bestOverallExplanation;
        private String bestValueExplanation;
        private String bestCameraExplanation;
        private String bestPerformanceExplanation;
        private String bestBatteryExplanation;

        public Builder products(List<ProductCardDto> products) { this.products = products; return this; }
        public Builder specGroups(List<String> specGroups) { this.specGroups = specGroups; return this; }
        public Builder specMatrix(Map<String, Map<Long, String>> specMatrix) { this.specMatrix = specMatrix; return this; }
        public Builder bestOverallId(Long bestOverallId) { this.bestOverallId = bestOverallId; return this; }
        public Builder bestValueId(Long bestValueId) { this.bestValueId = bestValueId; return this; }
        public Builder bestCameraId(Long bestCameraId) { this.bestCameraId = bestCameraId; return this; }
        public Builder bestPerformanceId(Long bestPerformanceId) { this.bestPerformanceId = bestPerformanceId; return this; }
        public Builder bestBatteryId(Long bestBatteryId) { this.bestBatteryId = bestBatteryId; return this; }
        public Builder aiSummary(String aiSummary) { this.aiSummary = aiSummary; return this; }
        public Builder bestOverallExplanation(String bestOverallExplanation) { this.bestOverallExplanation = bestOverallExplanation; return this; }
        public Builder bestValueExplanation(String bestValueExplanation) { this.bestValueExplanation = bestValueExplanation; return this; }
        public Builder bestCameraExplanation(String bestCameraExplanation) { this.bestCameraExplanation = bestCameraExplanation; return this; }
        public Builder bestPerformanceExplanation(String bestPerformanceExplanation) { this.bestPerformanceExplanation = bestPerformanceExplanation; return this; }
        public Builder bestBatteryExplanation(String bestBatteryExplanation) { this.bestBatteryExplanation = bestBatteryExplanation; return this; }

        public ProductComparisonDto build() {
            return new ProductComparisonDto(products, specGroups, specMatrix, bestOverallId, bestValueId, bestCameraId, bestPerformanceId, bestBatteryId, aiSummary, bestOverallExplanation, bestValueExplanation, bestCameraExplanation, bestPerformanceExplanation, bestBatteryExplanation);
        }
    }

    public List<ProductCardDto> getProducts() { return products; }
    public void setProducts(List<ProductCardDto> products) { this.products = products; }

    public List<String> getSpecGroups() { return specGroups; }
    public void setSpecGroups(List<String> specGroups) { this.specGroups = specGroups; }

    public Map<String, Map<Long, String>> getSpecMatrix() { return specMatrix; }
    public void setSpecMatrix(Map<String, Map<Long, String>> specMatrix) { this.specMatrix = specMatrix; }

    public Long getBestOverallId() { return bestOverallId; }
    public void setBestOverallId(Long bestOverallId) { this.bestOverallId = bestOverallId; }

    public Long getBestValueId() { return bestValueId; }
    public void setBestValueId(Long bestValueId) { this.bestValueId = bestValueId; }

    public Long getBestCameraId() { return bestCameraId; }
    public void setBestCameraId(Long bestCameraId) { this.bestCameraId = bestCameraId; }

    public Long getBestPerformanceId() { return bestPerformanceId; }
    public void setBestPerformanceId(Long bestPerformanceId) { this.bestPerformanceId = bestPerformanceId; }

    public Long getBestBatteryId() { return bestBatteryId; }
    public void setBestBatteryId(Long bestBatteryId) { this.bestBatteryId = bestBatteryId; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public String getBestOverallExplanation() { return bestOverallExplanation; }
    public void setBestOverallExplanation(String bestOverallExplanation) { this.bestOverallExplanation = bestOverallExplanation; }

    public String getBestValueExplanation() { return bestValueExplanation; }
    public void setBestValueExplanation(String bestValueExplanation) { this.bestValueExplanation = bestValueExplanation; }

    public String getBestCameraExplanation() { return bestCameraExplanation; }
    public void setBestCameraExplanation(String bestCameraExplanation) { this.bestCameraExplanation = bestCameraExplanation; }

    public String getBestPerformanceExplanation() { return bestPerformanceExplanation; }
    public void setBestPerformanceExplanation(String bestPerformanceExplanation) { this.bestPerformanceExplanation = bestPerformanceExplanation; }

    public String getBestBatteryExplanation() { return bestBatteryExplanation; }
    public void setBestBatteryExplanation(String bestBatteryExplanation) { this.bestBatteryExplanation = bestBatteryExplanation; }
}
