package com.example.aistore.repository;

import com.example.aistore.entity.AIRecommendationLog;
import com.example.aistore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRecommendationLogRepository extends JpaRepository<AIRecommendationLog, Long> {
    List<AIRecommendationLog> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<AIRecommendationLog> findTop20ByOrderByCreatedAtDesc();
}
