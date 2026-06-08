package com.example.classicreading.repository;

import com.example.classicreading.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 对话数据访问接口
 * 提供对 Conversation 实体的数据库操作
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * 根据用户ID查询该用户的所有对话，按创建时间倒序排序
     * @param userId 用户ID
     * @return 对话列表
     */
    List<Conversation> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据类型查询所有对话，按创建时间倒序排序
     * @param type 对话类型（community 或 ai）
     * @return 对话列表
     */
    List<Conversation> findByTypeOrderByCreatedAtDesc(String type);

    /**
     * 根据类型分页查询对话，按创建时间倒序排序
     * @param type 对话类型
     * @param pageable 分页参数
     * @return 分页对话数据
     */
    Page<Conversation> findByTypeOrderByCreatedAtDesc(String type, Pageable pageable);

    /**
     * 根据ID删除对话（显式声明，实际继承自 JpaRepository 的 deleteById 方法已可用）
     * @param id 对话ID
     */
    void deleteById(Long id);

    List<Conversation> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);
}