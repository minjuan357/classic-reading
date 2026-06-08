package com.example.classicreading.controller.api;

import com.example.classicreading.entity.Conversation;
import com.example.classicreading.entity.Message;
import com.example.classicreading.entity.User;
import com.example.classicreading.service.ConversationService;
import com.example.classicreading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    /**
     * 分页获取社区主题列表
     */
    @GetMapping("/conversations")
    public ResponseEntity<Page<ConversationDto>> getConversations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        Page<Conversation> conversations = conversationService.getConversations(page, size, sort);
        Page<ConversationDto> dtoPage = conversations.map(this::convertToDto);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * 创建新的社区主题
     */
    @PostMapping("/conversations")
    public ResponseEntity<ConversationDto> createConversation(
            @RequestBody CreateConversationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Conversation conversation = conversationService.createConversation(
                request.getTitle(), request.getContent(), request.getCategory(), user);
        return ResponseEntity.ok(convertToDto(conversation));
    }

    /**
     * 获取单个社区主题详情（包含所有回复）
     */
    @GetMapping("/conversations/{id}")
    @Transactional
    public Conversation getConversation(@PathVariable Long id) {
        Conversation conv = conversationService.getConversationById(id);
        // 强制初始化 messages 集合，避免序列化时懒加载异常
        conv.getMessages().size();
        return conv;
    }

    /**
     * 获取指定主题的所有回复消息（按时间升序）
     */
    @GetMapping("/conversations/{id}/messages")
    public List<Message> getMessages(@PathVariable Long id) {
        return conversationService.getMessagesByConversationId(id);
    }

    /**
     * 对指定主题添加回复
     * 修改参数接收方式：接收 JSON 对象，兼容前端 { content: "xxx" } 格式
     */
    @PostMapping("/conversations/{id}/messages")
    public Message reply(@PathVariable Long id,
                         @RequestBody Map<String, String> payload,
                         @AuthenticationPrincipal UserDetails userDetails) {
        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("回复内容不能为空");
        }
        User user = userService.findByUsername(userDetails.getUsername());
        return conversationService.addReply(id, content, user);
    }

    // ---------- 以下为辅助方法及内部类 ----------

    private ConversationDto convertToDto(Conversation c) {
        ConversationDto dto = new ConversationDto();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setCreatorName(c.getUser().getUsername());
        dto.setCategory(c.getCategory());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setMessageCount(c.getMessages() != null ? c.getMessages().size() : 0);
        return dto;
    }

    public static class CreateConversationRequest {
        private String title;
        private String content;
        private String category;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class ConversationDto {
        private Long id;
        private String title;
        private String creatorName;
        private String category;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int messageCount;

        // getters / setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getCreatorName() { return creatorName; }
        public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public int getMessageCount() { return messageCount; }
        public void setMessageCount(int messageCount) { this.messageCount = messageCount; }
    }
}