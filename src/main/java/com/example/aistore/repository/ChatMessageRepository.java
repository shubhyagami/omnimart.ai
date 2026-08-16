package com.example.aistore.repository;

import com.example.aistore.entity.ChatConversation;
import com.example.aistore.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversationOrderByCreatedAtAsc(ChatConversation conversation);
}
