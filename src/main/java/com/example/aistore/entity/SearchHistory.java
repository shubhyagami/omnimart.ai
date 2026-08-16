package com.example.aistore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_histories")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String sessionId;

    @Column(nullable = false)
    private String searchQuery;

    @Column(columnDefinition = "TEXT")
    private String filtersAppliedJson;

    private int resultCount;

    @CreationTimestamp
    private LocalDateTime searchedAt;

    public SearchHistory() {}

    public SearchHistory(Long id, User user, String sessionId, String searchQuery, String filtersAppliedJson, int resultCount, LocalDateTime searchedAt) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.searchQuery = searchQuery;
        this.filtersAppliedJson = filtersAppliedJson;
        this.resultCount = resultCount;
        this.searchedAt = searchedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String sessionId;
        private String searchQuery;
        private String filtersAppliedJson;
        private int resultCount;
        private LocalDateTime searchedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder searchQuery(String searchQuery) { this.searchQuery = searchQuery; return this; }
        public Builder filtersAppliedJson(String filtersAppliedJson) { this.filtersAppliedJson = filtersAppliedJson; return this; }
        public Builder resultCount(int resultCount) { this.resultCount = resultCount; return this; }
        public Builder searchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; return this; }

        public SearchHistory build() {
            return new SearchHistory(id, user, sessionId, searchQuery, filtersAppliedJson, resultCount, searchedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getFiltersAppliedJson() { return filtersAppliedJson; }
    public void setFiltersAppliedJson(String filtersAppliedJson) { this.filtersAppliedJson = filtersAppliedJson; }

    public int getResultCount() { return resultCount; }
    public void setResultCount(int resultCount) { this.resultCount = resultCount; }

    public LocalDateTime getSearchedAt() { return searchedAt; }
    public void setSearchedAt(LocalDateTime searchedAt) { this.searchedAt = searchedAt; }
}
