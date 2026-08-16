package com.example.aistore.repository;

import com.example.aistore.entity.ChatConversation;
import com.example.aistore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    Optional<ChatConversation> findByConversationId(String conversationId);
    List<ChatConversation> findByUserOrderByUpdatedAtDesc(User user);
    List<ChatConversation> findBySessionIdOrderByUpdatedAtDesc(String sessionId);
}
