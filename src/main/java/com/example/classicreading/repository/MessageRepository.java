package com.example.classicreading.repository;

import com.example.classicreading.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    /**
     * 根据对话 ID 删除所有消息
     * @param conversationId 对话 ID
     */
    void deleteByConversationId(Long conversationId);
}
