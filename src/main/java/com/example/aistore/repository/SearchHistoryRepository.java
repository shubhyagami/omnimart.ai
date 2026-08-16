package com.example.aistore.repository;

import com.example.aistore.entity.SearchHistory;
import com.example.aistore.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchedAtDesc(User user, Pageable pageable);
    List<SearchHistory> findBySessionIdOrderBySearchedAtDesc(String sessionId, Pageable pageable);

    @Query("SELECT s.searchQuery, COUNT(s) FROM SearchHistory s GROUP BY s.searchQuery ORDER BY COUNT(s) DESC")
    List<Object[]> findTopTrendingSearches(Pageable pageable);
}
