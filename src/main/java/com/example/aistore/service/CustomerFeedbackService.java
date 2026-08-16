package com.example.aistore.service;

import com.example.aistore.ai.MockAIProvider;
import com.example.aistore.dto.FeedbackAnalysisDto;
import com.example.aistore.entity.CustomerFeedback;
import com.example.aistore.entity.Product;
import com.example.aistore.entity.Review;
import com.example.aistore.repository.CustomerFeedbackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerFeedbackService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomerFeedbackService.class);


    private final CustomerFeedbackRepository feedbackRepository;
    private final MockAIProvider mockAIProvider;
    private final ObjectMapper objectMapper;
    public CustomerFeedbackService(CustomerFeedbackRepository feedbackRepository, MockAIProvider mockAIProvider, ObjectMapper objectMapper) {
        this.feedbackRepository = feedbackRepository;
        this.mockAIProvider = mockAIProvider;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public CustomerFeedback processReviewFeedback(Review review) {
        FeedbackAnalysisDto analysis = mockAIProvider.analyzeCustomerFeedback(review.getTitle(), review.getComment(), review.getRating());

        String issuesJson = "[]";
        String positivesJson = "[]";
        try {
            if (analysis.getSpecificIssues() != null) {
                issuesJson = objectMapper.writeValueAsString(analysis.getSpecificIssues());
            }
            if (analysis.getPositiveAspects() != null) {
                positivesJson = objectMapper.writeValueAsString(analysis.getPositiveAspects());
            }
        } catch (Exception ignored) {}

        CustomerFeedback feedback = CustomerFeedback.builder()
                .review(review)
                .product(review.getProduct())
                .user(review.getUser())
                .sentiment(analysis.getSentiment())
                .emotion(analysis.getEmotion())
                .primaryTopic(analysis.getPrimaryTopic())
                .specificIssuesJson(issuesJson)
                .positiveAspectsJson(positivesJson)
                .confidenceScore(analysis.getConfidenceScore())
                .source("REVIEW")
                .build();

        return feedbackRepository.save(feedback);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getSentimentDistribution() {
        List<Object[]> rows = feedbackRepository.countBySentimentGroup();
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("Positive", 0L);
        map.put("Negative", 0L);
        map.put("Mixed", 0L);
        map.put("Neutral", 0L);
        for (Object[] row : rows) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getTopNegativeComplaintTopics() {
        List<Object[]> rows = feedbackRepository.countNegativeIssuesByTopic();
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            map.put((String) row[0], (Long) row[1]);
        }
        return map;
    }

    @Transactional(readOnly = true)
    public List<Object[]> getProductsWithMostNegativeFeedback(int limit) {
        return feedbackRepository.findProductsWithMostNegativeFeedback(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<CustomerFeedback> getRecentFeedbacks(int limit) {
        return feedbackRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
