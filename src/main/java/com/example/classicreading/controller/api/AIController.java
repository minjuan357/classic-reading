package com.example.classicreading.controller.api;

import com.example.classicreading.entity.AiRequest;
import com.example.classicreading.entity.AiResponse;
import com.example.classicreading.entity.Conversation;
import com.example.classicreading.entity.Message;
import com.example.classicreading.service.AiService;
import com.example.classicreading.service.ConversationService;
import com.example.classicreading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AiService aiService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserService userService;

    // 获取当前登录用户ID
    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername()).getId();
    }

    @GetMapping("/conversations")
    public List<Conversation> getConversations() {
        return conversationService.getAiConversationsByUserId(getCurrentUserId());
    }

    @GetMapping("/conversations/{convId}/messages")
    public List<Message> getMessages(@PathVariable Long convId) {
        return conversationService.getMessagesByConversationId(convId);
    }

    @PostMapping("/conversations")
    public Conversation createConversation(@RequestBody(required = false) Map<String, String> payload) {
        String title = (payload != null && payload.containsKey("title")) ? payload.get("title") : "新对话";
        return conversationService.createConversation(getCurrentUserId(), title);
    }

    @DeleteMapping("/conversations/{convId}")
    public void deleteConversation(@PathVariable Long convId) {
        conversationService.deleteConversation(convId);
    }

    // ===================== 修复：返回 Map，彻底避免序列化循环 =====================
    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> chat(@RequestBody AiRequest request) {
        AiResponse aiResponse = aiService.getAiReply(request, getCurrentUserId());
        return Map.of(
                "reply", aiResponse.getReply(),
                "sessionId", aiResponse.getSessionId(),
                "conversationId", aiResponse.getConversationId()
        );
    }
}