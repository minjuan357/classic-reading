package com.example.classicreading.service;

import com.example.classicreading.entity.AiRequest;
import com.example.classicreading.entity.AiResponse;
import com.example.classicreading.entity.Conversation;
import com.example.classicreading.entity.Message;
import com.example.classicreading.repository.MessageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private MessageRepository messageRepository;

    @Value("${zhipu.api.base-url}")
    private String baseUrl;

    @Value("${zhipu.api.api-key}")
    private String apiKey;

    @Value("${zhipu.api.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取 AI 回复，集成对话保存和深度思考逻辑
     * @param request 包含消息、会话 ID、对话 ID、深度思考标志
     * @param userId 用户 ID（可为空，用于创建新对话）
     * @return AiResponse 包含回复和对话 ID
     */
    public AiResponse getAiReply(AiRequest request, Long userId) {
        Long convId = request.getConversationId();
        String message = request.getMessage();
        boolean deepThinking = request.isDeepThinking();

        // 如果未传入 conversationId 且 userId 非空，创建一个新对话
        if (convId == null && userId != null) {
            Conversation newConv = conversationService.createConversation(userId, "新对话");
            convId = newConv.getId();
        }

        // 保存用户消息（如果有对话 ID）
        if (convId != null) {
            conversationService.saveMessage(convId, "user", message);

            // ===================== 首次消息自动更新标题 =====================
            List<Message> existingMessages = conversationService.getMessagesByConversationId(convId);
            if (existingMessages.size() == 1) {
                conversationService.updateConversationTitle(convId, message);
            }
            // ====================================================================
        }

        // 加载历史消息列表，用于拼接上下文
        List<Message> historyMessages = null;
        if (convId != null) {
            historyMessages = conversationService.getMessagesByConversationId(convId);
        }

        // 调用大模型，根据 deepThinking 调整参数
        String reply;
        try {
            reply = callZhipuApi(message, deepThinking, historyMessages);
        } catch (Exception e) {
            e.printStackTrace();
            reply = getFallbackReply(message);
        }

        // 保存助手消息（如果有对话 ID）
        if (convId != null) {
            conversationService.saveMessage(convId, "assistant", reply);
        }

        // 将 Long 类型的 convId 转换为 String 传递给 sessionId（兼容旧字段）
        String sessionId = convId != null ? String.valueOf(convId) : null;
        // 使用全参构造函数，确保 conversationId 字段被填充
        return new AiResponse(reply, sessionId, convId);
    }

    /**
     * 新增重载方法：适配 AIController 中传递两个 String 参数的调用
     * @param message 用户消息
     * @param conversationIdStr 会话 ID 字符串
     * @return AiResponse 包含回复和对话 ID
     */
    public AiResponse getAiReply(String message, String conversationIdStr) {
        Long conversationId = null;
        if (conversationIdStr != null && !conversationIdStr.isEmpty()) {
            try {
                conversationId = Long.parseLong(conversationIdStr);
            } catch (NumberFormatException e) {
                // 如果转换失败，可记录日志并抛出异常，或使用 null 继续
                throw new IllegalArgumentException("无效的会话 ID 格式：" + conversationIdStr);
            }
        }

        AiRequest request = new AiRequest();
        request.setMessage(message);
        request.setConversationId(conversationId);
        request.setDeepThinking(false); // 默认深度思考为 false，可根据需要调整

        // 调用原方法，userId 传 null（因为已有 conversationId，无需创建新对话）
        return getAiReply(request, null);
    }

    /**
     * 调用智谱 AI API，支持深度思考参数调整和对话历史拼接
     */
    private String callZhipuApi(String message, boolean deepThinking, List<Message> historyMessages) throws Exception {
        System.out.println("【调试】正在调用智谱 API，模型：" + model + "，消息：" + message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", model);

        ArrayNode messages = requestBody.putArray("messages");

        // 系统提示
        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一位精通中国古代典籍的专家，请用专业但易懂的方式回答用户关于古籍的问题。回答要准确、有依据，可引用原文。");

        // 拼接历史上下文（排除最后一条用户消息，避免重复）
        if (historyMessages != null) {
            for (int i = 0; i < historyMessages.size(); i++) {
                Message msg = historyMessages.get(i);
                // 跳过当前用户消息（已在历史中）
                if (i == historyMessages.size() - 1 && "user".equals(msg.getRole()) && msg.getContent().equals(message)) {
                    continue;
                }
                ObjectNode historyNode = messages.addObject();
                historyNode.put("role", msg.getRole());
                historyNode.put("content", msg.getContent());
            }
        }

        // 当前用户消息
        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);

        // 根据深度思考调整参数
        if (deepThinking) {
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 2000);
        } else {
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1500);
        }

        HttpEntity<ObjectNode> entity = new HttpEntity<>(requestBody, headers);

        String apiUrl = baseUrl + "/chat/completions";
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                JsonNode.class
        );

        JsonNode responseBody = response.getBody();

        // ===================== 新增：API 响应成功日志 =====================
        if (responseBody != null) {
            System.out.println("【调试】API 响应成功，返回内容长度：" + responseBody.toString().length());
        }

        if (responseBody != null && responseBody.has("choices")) {
            JsonNode firstChoice = responseBody.get("choices").get(0);
            JsonNode messageNode = firstChoice.get("message");
            return messageNode.get("content").asText();
        }

        throw new RuntimeException("API 返回格式异常");
    }

    /**
     * 降级回复：当 API 不可用时使用规则匹配（优化版）
     */
    private String getFallbackReply(String message) {
        if (message.contains("论语")) {
            return "《论语》是儒家经典，记录了孔子及其弟子的言行。您想了解哪一篇章？如“学而篇”、“为政篇”等。";
        } else if (message.contains("翻译")) {
            return "请提供您需要翻译的古文句子，我会尽力帮您转换成白话文。";
        } else if (message.contains("帮助")) {
            return "您可以问我古籍原文、白话翻译、作者背景、名句解读等问题。试试“介绍《史记》”或“《道德经》第一章翻译”。";
        } else {
            return "您好，我是古籍智能助手。您可以询问经典内容、翻译或推荐书目，我会尽力解答。";
        }
    }
}