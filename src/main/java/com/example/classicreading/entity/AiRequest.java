package com.example.classicreading.entity;

public class AiRequest {
    private String message;
    private String sessionId;          // 可选，用于多轮对话
    private Long conversationId;        // 对话ID，用于关联多轮对话
    private boolean deepThinking;       // 是否开启深度思考模式

    // 无参构造（必需）
    public AiRequest() {}

    // Getter 和 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isDeepThinking() {
        return deepThinking;
    }

    public void setDeepThinking(boolean deepThinking) {
        this.deepThinking = deepThinking;
    }
}