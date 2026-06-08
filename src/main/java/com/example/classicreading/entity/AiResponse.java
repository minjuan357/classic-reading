package com.example.classicreading.entity;

public class AiResponse {
    private String reply;
    private String sessionId;
    private Long conversationId;

    public AiResponse() {}

    public AiResponse(String reply, String sessionId) {
        this.reply = reply;
        this.sessionId = sessionId;
    }

    public AiResponse(String reply, String sessionId, Long conversationId) {
        this.reply = reply;
        this.sessionId = sessionId;
        this.conversationId = conversationId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
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
}