package com.example.aistore.service;

import com.example.aistore.entity.Product;
import com.example.aistore.entity.Review;
import com.example.aistore.entity.User;
import com.example.aistore.entity.UserInteraction;
import com.example.aistore.repository.OrderItemRepository;
import com.example.aistore.repository.ProductRepository;
import com.example.aistore.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerFeedbackService feedbackService;
    private final UserInteractionService interactionService;
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository, CustomerFeedbackService feedbackService, UserInteractionService interactionService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.feedbackService = feedbackService;
        this.interactionService = interactionService;
    }


    @Transactional
    public Review addReview(User user, Long productId, int rating, String title, String comment) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        boolean verified = orderItemRepository.existsByUserIdAndProductId(user.getId(), productId);

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(rating)
                .title(title)
                .comment(comment)
                .verifiedPurchase(verified)
                .sentiment(rating >= 4 ? "POSITIVE" : (rating <= 2 ? "NEGATIVE" : "MIXED"))
                .build();

        review = reviewRepository.save(review);

        // Update product average rating and review count
        Double avgRating = reviewRepository.calculateAverageRating(productId);
        int totalReviews = reviewRepository.countReviewsByProductId(productId);
        product.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : rating);
        product.setReviewCount(totalReviews);
        productRepository.save(product);

        // Process Customer Feedback Intelligence
        feedbackService.processReviewFeedback(review);

        // Record behavioral telemetry
        interactionService.recordInteraction(user.getId(), null, UserInteraction.InteractionType.PRODUCT_REVIEW, productId, 0, null);

        return review;
    }

    @Transactional(readOnly = true)
    public List<Review> getProductReviews(Product product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    @Transactional(readOnly = true)
    public Page<Review> getProductReviewsPaged(Product product, Pageable pageable) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product, pageable);
    }
}
