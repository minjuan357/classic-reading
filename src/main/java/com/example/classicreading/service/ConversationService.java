package com.example.classicreading.service;

import com.example.classicreading.entity.Conversation;
import com.example.classicreading.entity.Message;
import com.example.classicreading.entity.User;
import com.example.classicreading.repository.ConversationRepository;
import com.example.classicreading.repository.MessageRepository;
import com.example.classicreading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 分页获取社区主题列表（类型为 community）
     * @param page 页码（从 0 开始）
     * @param size 每页条数
     * @param sort 排序字段（createdAt 或 likeCount），null 时默认按创建时间
     * @return 分页的 Conversation 列表
     */
    public Page<Conversation> getConversations(int page, int size, String sort) {
        Sort sortObj = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return conversationRepository.findByTypeOrderByCreatedAtDesc("community", pageable);
    }

    /**
     * 创建社区主题，并自动生成第一条消息（主题内容）
     * @param title 标题
     * @param content 内容（将作为第一条消息）
     * @param category 分类
     * @param user 创建者
     * @return 创建的 Conversation
     */
    @Transactional
    public Conversation createConversation(String title, String content, String category, User user) {
        // 1. 创建并保存 Conversation
        Conversation conv = new Conversation();
        conv.setUser(user);
        conv.setTitle(title);
        conv.setContent(content);
        conv.setCategory(category);
        conv.setType("community");
        conv.setCreatedAt(LocalDateTime.now());
        conv.setUpdatedAt(LocalDateTime.now());
        Conversation savedConv = conversationRepository.save(conv);

        // 2. 创建并保存初始 Message
        Message msg = new Message();
        msg.setConversation(savedConv);
        msg.setUser(user);
        msg.setRole("user");
        msg.setContent(content);
        msg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(msg);

        return savedConv;
    }

    /**
     * 根据 ID 获取主题或对话
     * @param id 对话 ID
     * @return Conversation 实体
     */
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("对话不存在"));
    }

    /**
     * 添加回复（针对社区主题）
     * @param conversationId 主题 ID
     * @param content 回复内容
     * @param user 回复者
     * @return 保存后的 Message
     */
    @Transactional
    public Message addReply(Long conversationId, String content, User user) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("主题不存在"));

        Message msg = new Message();
        msg.setConversation(conversation);
        msg.setUser(user);
        msg.setRole("user");
        msg.setContent(content);
        msg.setCreatedAt(LocalDateTime.now());

        // 更新主题的更新时间
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return messageRepository.save(msg);
    }

    /**
     * 根据用户 ID 获取该用户的所有对话
     * @param userId 用户 ID
     * @return 对话列表
     */
    public List<Conversation> getConversationsByUserId(Long userId) {
        return conversationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Conversation> getAiConversationsByUserId(Long userId) {
        return conversationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, "ai");
    }

    /**
     * 创建 AI 对话（仅标题，无正文）
     * @param userId 用户 ID
     * @param title 对话标题（可由系统自动生成）
     * @return 创建的 Conversation
     */
    public Conversation createConversation(Long userId, String title) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Conversation conv = new Conversation();
        conv.setUser(user);
        conv.setTitle(title);
        conv.setType("ai");          // 必须设置类型
        conv.setCreatedAt(LocalDateTime.now());
        conv.setUpdatedAt(LocalDateTime.now());
        return conversationRepository.save(conv);
    }

    /**
     * 保存消息到指定对话（用于 AI 对话）
     * @param conversationId 对话 ID
     * @param role 角色（user 或 assistant）
     * @param content 消息内容
     * @return 保存后的 Message
     */
    @Transactional
    public Message saveMessage(Long conversationId, String role, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("对话不存在"));

        Message msg = new Message();
        msg.setConversation(conversation);
        msg.setRole(role);
        msg.setContent(content);
        msg.setCreatedAt(LocalDateTime.now());

        // 兼容 conversation 表中的冗余字段，避免导入库存在非空约束时写入失败
        if ("user".equalsIgnoreCase(role)) {
            conversation.setUserMessage(content);
        } else if ("assistant".equalsIgnoreCase(role)) {
            conversation.setAiReply(content);
        }

        // 更新对话的最后更新时间
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return messageRepository.save(msg);
    }

    /**
     * 获取指定对话的所有消息，按创建时间升序排列
     * @param conversationId 对话 ID
     * @return 消息列表
     */
    public List<Message> getMessagesByConversationId(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    /**
     * 删除对话（级联删除关联的所有消息）
     * @param conversationId 对话 ID
     */
    public void deleteConversation(Long conversationId) {
        // 由于 Conversation 中配置了 CascadeType.ALL，直接删除对话即可自动删除关联消息
        conversationRepository.deleteById(conversationId);
    }

    // ===================== 确认已存在：更新对话标题方法 =====================
    /**
     * 根据第一条消息内容自动更新对话标题
     * @param conversationId 对话ID
     * @param firstMessage 第一条消息内容
     */
    @Transactional
    public void updateConversationTitle(Long conversationId, String firstMessage) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("对话不存在"));
        // 截取前15个字符作为标题，去除换行
        String title = firstMessage.length() > 15 ? firstMessage.substring(0, 15) + "…" : firstMessage;
        conv.setTitle(title);
        conversationRepository.save(conv);
    }
}