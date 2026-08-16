package com.example.aistore.repository;

import com.example.aistore.entity.User;
import com.example.aistore.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByUser(User user);
    Optional<UserPreference> findByUserId(Long userId);
}
