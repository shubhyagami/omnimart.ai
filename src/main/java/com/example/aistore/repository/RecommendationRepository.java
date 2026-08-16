package com.example.aistore.repository;

import com.example.aistore.entity.Recommendation;
import com.example.aistore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserOrderByHybridScoreDesc(User user, Pageable pageable);
    void deleteByUser(User user);
}
