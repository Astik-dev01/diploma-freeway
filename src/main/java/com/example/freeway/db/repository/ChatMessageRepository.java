package com.example.freeway.db.repository;

import com.example.freeway.db.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            Long senderId, Long recipientId, Long recipientId2, Long senderId2
    );

    List<ChatMessage> findAllBySenderIdOrRecipientId(Long userId, Long userId1);
}
