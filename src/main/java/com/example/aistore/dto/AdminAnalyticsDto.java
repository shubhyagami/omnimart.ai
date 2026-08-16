package com.example.aistore.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AdminAnalyticsDto {
    private BigDecimal totalRevenue;
    private long totalOrders;
    private long totalProducts;
    private long totalUsers;
    private double positiveFeedbackPercentage;
    private Map<String, Long> sentimentDistribution;
    private Map<String, Long> topComplaintTopics;
    private List<ProductMetricDto> mostViewedProducts;
    private List<ProductMetricDto> mostReturnedOrComplainedProducts;
    private List<ProductMetricDto> highViewsLowSalesProducts;
    private List<String> churnSignalsAndOpportunities;

    public AdminAnalyticsDto() {}

    public AdminAnalyticsDto(BigDecimal totalRevenue, long totalOrders, long totalProducts, long totalUsers, double positiveFeedbackPercentage, Map<String, Long> sentimentDistribution, Map<String, Long> topComplaintTopics, List<ProductMetricDto> mostViewedProducts, List<ProductMetricDto> mostReturnedOrComplainedProducts, List<ProductMetricDto> highViewsLowSalesProducts, List<String> churnSignalsAndOpportunities) {
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.totalProducts = totalProducts;
        this.totalUsers = totalUsers;
        this.positiveFeedbackPercentage = positiveFeedbackPercentage;
        this.sentimentDistribution = sentimentDistribution;
        this.topComplaintTopics = topComplaintTopics;
        this.mostViewedProducts = mostViewedProducts;
        this.mostReturnedOrComplainedProducts = mostReturnedOrComplainedProducts;
        this.highViewsLowSalesProducts = highViewsLowSalesProducts;
        this.churnSignalsAndOpportunities = churnSignalsAndOpportunities;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BigDecimal totalRevenue;
        private long totalOrders;
        private long totalProducts;
        private long totalUsers;
        private double positiveFeedbackPercentage;
        private Map<String, Long> sentimentDistribution;
        private Map<String, Long> topComplaintTopics;
        private List<ProductMetricDto> mostViewedProducts;
        private List<ProductMetricDto> mostReturnedOrComplainedProducts;
        private List<ProductMetricDto> highViewsLowSalesProducts;
        private List<String> churnSignalsAndOpportunities;

        public Builder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public Builder totalOrders(long totalOrders) { this.totalOrders = totalOrders; return this; }
        public Builder totalProducts(long totalProducts) { this.totalProducts = totalProducts; return this; }
        public Builder totalUsers(long totalUsers) { this.totalUsers = totalUsers; return this; }
        public Builder positiveFeedbackPercentage(double positiveFeedbackPercentage) { this.positiveFeedbackPercentage = positiveFeedbackPercentage; return this; }
        public Builder sentimentDistribution(Map<String, Long> sentimentDistribution) { this.sentimentDistribution = sentimentDistribution; return this; }
        public Builder topComplaintTopics(Map<String, Long> topComplaintTopics) { this.topComplaintTopics = topComplaintTopics; return this; }
        public Builder mostViewedProducts(List<ProductMetricDto> mostViewedProducts) { this.mostViewedProducts = mostViewedProducts; return this; }
        public Builder mostReturnedOrComplainedProducts(List<ProductMetricDto> mostReturnedOrComplainedProducts) { this.mostReturnedOrComplainedProducts = mostReturnedOrComplainedProducts; return this; }
        public Builder highViewsLowSalesProducts(List<ProductMetricDto> highViewsLowSalesProducts) { this.highViewsLowSalesProducts = highViewsLowSalesProducts; return this; }
        public Builder churnSignalsAndOpportunities(List<String> churnSignalsAndOpportunities) { this.churnSignalsAndOpportunities = churnSignalsAndOpportunities; return this; }

        public AdminAnalyticsDto build() {
            return new AdminAnalyticsDto(totalRevenue, totalOrders, totalProducts, totalUsers, positiveFeedbackPercentage, sentimentDistribution, topComplaintTopics, mostViewedProducts, mostReturnedOrComplainedProducts, highViewsLowSalesProducts, churnSignalsAndOpportunities);
        }
    }

    public static class ProductMetricDto {
        private Long productId;
        private String productName;
        private long count;
        private double rate;
        private String note;

        public ProductMetricDto() {}

        public ProductMetricDto(Long productId, String productName, long count, double rate, String note) {
            this.productId = productId;
            this.productName = productName;
            this.count = count;
            this.rate = rate;
            this.note = note;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }

        public double getRate() { return rate; }
        public void setRate(double rate) { this.rate = rate; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public double getPositiveFeedbackPercentage() { return positiveFeedbackPercentage; }
    public void setPositiveFeedbackPercentage(double positiveFeedbackPercentage) { this.positiveFeedbackPercentage = positiveFeedbackPercentage; }

    public Map<String, Long> getSentimentDistribution() { return sentimentDistribution; }
    public void setSentimentDistribution(Map<String, Long> sentimentDistribution) { this.sentimentDistribution = sentimentDistribution; }

    public Map<String, Long> getTopComplaintTopics() { return topComplaintTopics; }
    public void setTopComplaintTopics(Map<String, Long> topComplaintTopics) { this.topComplaintTopics = topComplaintTopics; }

    public List<ProductMetricDto> getMostViewedProducts() { return mostViewedProducts; }
    public void setMostViewedProducts(List<ProductMetricDto> mostViewedProducts) { this.mostViewedProducts = mostViewedProducts; }

    public List<ProductMetricDto> getMostReturnedOrComplainedProducts() { return mostReturnedOrComplainedProducts; }
    public void setMostReturnedOrComplainedProducts(List<ProductMetricDto> mostReturnedOrComplainedProducts) { this.mostReturnedOrComplainedProducts = mostReturnedOrComplainedProducts; }

    public List<ProductMetricDto> getHighViewsLowSalesProducts() { return highViewsLowSalesProducts; }
    public void setHighViewsLowSalesProducts(List<ProductMetricDto> highViewsLowSalesProducts) { this.highViewsLowSalesProducts = highViewsLowSalesProducts; }

    public List<String> getChurnSignalsAndOpportunities() { return churnSignalsAndOpportunities; }
    public void setChurnSignalsAndOpportunities(List<String> churnSignalsAndOpportunities) { this.churnSignalsAndOpportunities = churnSignalsAndOpportunities; }
}
