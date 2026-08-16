package com.example.aistore.repository;

import com.example.aistore.entity.CustomerFeedback;
import com.example.aistore.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Long> {

    List<CustomerFeedback> findByProduct(Product product);

    List<CustomerFeedback> findByProductId(Long productId);

    List<CustomerFeedback> findBySentiment(String sentiment);

    @Query("SELECT f.sentiment, COUNT(f) FROM CustomerFeedback f GROUP BY f.sentiment")
    List<Object[]> countBySentimentGroup();

    @Query("SELECT f.primaryTopic, COUNT(f) FROM CustomerFeedback f WHERE f.sentiment = 'Negative' GROUP BY f.primaryTopic ORDER BY COUNT(f) DESC")
    List<Object[]> countNegativeIssuesByTopic();

    @Query("SELECT f.primaryTopic, COUNT(f) FROM CustomerFeedback f GROUP BY f.primaryTopic ORDER BY COUNT(f) DESC")
    List<Object[]> countAllByTopic();

    @Query("SELECT f.product.id, f.product.name, COUNT(f) FROM CustomerFeedback f WHERE f.sentiment = 'Negative' GROUP BY f.product.id, f.product.name ORDER BY COUNT(f) DESC")
    List<Object[]> findProductsWithMostNegativeFeedback(Pageable pageable);

    @Query("SELECT f FROM CustomerFeedback f WHERE f.product.id = :productId AND f.sentiment = :sentiment")
    List<CustomerFeedback> findByProductIdAndSentiment(@Param("productId") Long productId, @Param("sentiment") String sentiment);

    List<CustomerFeedback> findTop20ByOrderByCreatedAtDesc();
}
