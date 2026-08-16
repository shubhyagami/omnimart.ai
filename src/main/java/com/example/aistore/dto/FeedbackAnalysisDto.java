package com.example.aistore.dto;

import java.util.List;

public class FeedbackAnalysisDto {
    private String sentiment;
    private String emotion;
    private String primaryTopic;
    private List<String> specificIssues;
    private List<String> positiveAspects;
    private double confidenceScore;

    public FeedbackAnalysisDto() {}

    public FeedbackAnalysisDto(String sentiment, String emotion, String primaryTopic, List<String> specificIssues, List<String> positiveAspects, double confidenceScore) {
        this.sentiment = sentiment;
        this.emotion = emotion;
        this.primaryTopic = primaryTopic;
        this.specificIssues = specificIssues;
        this.positiveAspects = positiveAspects;
        this.confidenceScore = confidenceScore;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String sentiment;
        private String emotion;
        private String primaryTopic;
        private List<String> specificIssues;
        private List<String> positiveAspects;
        private double confidenceScore;

        public Builder sentiment(String sentiment) { this.sentiment = sentiment; return this; }
        public Builder emotion(String emotion) { this.emotion = emotion; return this; }
        public Builder primaryTopic(String primaryTopic) { this.primaryTopic = primaryTopic; return this; }
        public Builder specificIssues(List<String> specificIssues) { this.specificIssues = specificIssues; return this; }
        public Builder positiveAspects(List<String> positiveAspects) { this.positiveAspects = positiveAspects; return this; }
        public Builder confidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; return this; }

        public FeedbackAnalysisDto build() {
            return new FeedbackAnalysisDto(sentiment, emotion, primaryTopic, specificIssues, positiveAspects, confidenceScore);
        }
    }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public String getPrimaryTopic() { return primaryTopic; }
    public void setPrimaryTopic(String primaryTopic) { this.primaryTopic = primaryTopic; }

    public List<String> getSpecificIssues() { return specificIssues; }
    public void setSpecificIssues(List<String> specificIssues) { this.specificIssues = specificIssues; }

    public List<String> getPositiveAspects() { return positiveAspects; }
    public void setPositiveAspects(List<String> positiveAspects) { this.positiveAspects = positiveAspects; }

    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
}
