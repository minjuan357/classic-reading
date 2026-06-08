<template>
  <div class="ai-sidebar" :class="{ expanded: isExpanded }">
    <!-- 折叠状态的悬浮按钮 -->
    <div v-if="!isExpanded" class="ai-toggle-btn" @click="isExpanded = true">
      <span class="ai-icon">🤖</span>
      <span class="ai-text">AI助手</span>
    </div>

    <!-- 展开状态的侧边栏 -->
    <div v-else class="ai-sidebar-expanded">
      <!-- 头部：返回按钮 + 标题 -->
      <div class="ai-header">
        <button class="back-btn" @click="isExpanded = false">← 返回</button>
        <h3>AI伴学助手</h3>
        <div class="header-placeholder"></div>
      </div>

      <!-- 工具栏 -->
      <div class="ai-toolbar">
        <button class="tool-btn" @click="newChat">新对话</button>
        <button class="tool-btn" @click="toggleHistory">历史记录</button>
        <button class="tool-btn" :class="{ active: deepThinking }" @click="toggleDeepThinking">深度思考</button>
      </div>

      <!-- 历史记录列表 -->
      <div v-if="showHistory" class="history-list">
        <div v-if="conversations.length === 0" class="history-empty">暂无历史记录</div>
        <div v-for="conv in conversations" :key="conv.id" class="history-item" @click="loadConversation(conv.id)">
          {{ conv.title }}
        </div>
      </div>

      <!-- 消息列表 -->
      <div class="messages" ref="messagesContainer">
        <div v-if="messages.length === 0 && !isThinking" class="empty-chat">
          <div class="empty-icon">🤖</div>
          <p>你好！请告诉我你想学习哪部典籍？</p>
        </div>
        <div v-for="(msg, index) in messages" :key="index" class="message" :class="msg.role">
          <div class="avatar">{{ msg.role === 'user' ? '👤' : '🤖' }}</div>
          <div class="bubble">{{ msg.content }}</div>
        </div>
        <div v-if="isThinking" class="message assistant">
          <div class="avatar">🤖</div>
          <div class="bubble thinking-bubble">
            <span class="thinking-text">正在思考</span>
            <span class="thinking-dots"><span>.</span><span>.</span><span>.</span></span>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <textarea v-model="inputText" placeholder="请输入您的内容~（Shift+Enter换行，Enter发送）" @keydown.enter.prevent="handleEnter" rows="1"></textarea>
        <button class="send-btn" @click="sendMessage">发送</button>
      </div>
      <div class="footer-note">注：内容由AI生成，可能有错误，使用前注意查证</div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue';
import request from '@/utils/request';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';

const userStore = useUserStore();
const router = useRouter();
const isExpanded = ref(false);
const showHistory = ref(false);
const inputText = ref('');
const messages = ref([
  { role: 'assistant', content: '你好，我是古籍智能助手，有什么可以帮您？' }
]);
const conversations = ref([]);
const currentConversationId = ref(null);
const deepThinking = ref(false);
const isThinking = ref(false);
const messagesContainer = ref(null);

// 获取历史对话列表
const fetchConversations = async () => {
  if (!userStore.token) return;
  try {
    const res = await request.get('/ai/conversations');
    conversations.value = res.data;
  } catch (error) {
    console.error('获取历史记录失败', error);
  }
};

// 切换历史记录（登录检查）
const toggleHistory = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再查看历史记录');
    router.push('/login');
    return;
  }
  showHistory.value = !showHistory.value;
  if (showHistory.value) fetchConversations();
};

// 创建新对话（登录检查）
const newChat = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再使用AI助手');
    router.push('/login');
    return;
  }
  try {
    const res = await request.post('/ai/conversations', { title: '新对话' });
    currentConversationId.value = res.data.id;
    messages.value = [{ role: 'assistant', content: '你好，我是古籍智能助手，有什么可以帮您？' }];
    showHistory.value = false;
  } catch (error) {
    console.error('创建新对话失败', error);
  }
};

// 加载指定历史对话的消息
const loadConversation = async (convId) => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再查看对话');
    router.push('/login');
    return;
  }
  try {
    const res = await request.get(`/ai/conversations/${convId}/messages`);
    messages.value = res.data.map(msg => ({ role: msg.role, content: msg.content }));
    currentConversationId.value = convId;
    showHistory.value = false;
    scrollToBottom();
  } catch (error) {
    console.error('加载对话失败', error);
  }
};

const toggleDeepThinking = () => {
  deepThinking.value = !deepThinking.value;
};

// 发送消息（登录检查 + 增强错误日志）
const sendMessage = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再使用AI助手');
    router.push('/login');
    return;
  }
  if (!inputText.value.trim()) return;
  const userMsg = { role: 'user', content: inputText.value };
  messages.value.push(userMsg);
  const question = inputText.value;
  inputText.value = '';
  scrollToBottom();

  isThinking.value = true;
  scrollToBottom();
  try {
    const res = await request.post('/ai/chat', {
      message: question,
      conversationId: currentConversationId.value,
      deepThinking: deepThinking.value
    });
    const aiMsg = { role: 'assistant', content: res.data.reply };
    messages.value.push(aiMsg);
    if (!currentConversationId.value && res.data.conversationId) {
      currentConversationId.value = res.data.conversationId;
    }
  } catch (error) {
    console.error('AI请求失败', error);
    if (error.response) {
      console.error('状态码:', error.response.status);
      console.error('响应数据:', error.response.data);
    } else if (error.request) {
      console.error('请求已发出但未收到响应:', error.request);
    } else {
      console.error('请求配置错误:', error.message);
    }
    messages.value.push({ role: 'assistant', content: '网络错误，请稍后重试。' });
  } finally {
    isThinking.value = false;
    scrollToBottom();
  }
};

const handleEnter = (e) => {
  if (!e.shiftKey) {
    e.preventDefault();
    sendMessage();
  }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
};

onMounted(() => {
  if (userStore.token) fetchConversations();
});
</script>

<style scoped>
/* 原有样式不变 */
.ai-sidebar {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  transition: all 0.3s ease;
}
.ai-sidebar.expanded {
  position: fixed;
  top: 0;
  right: 0;
  width: 380px;
  height: 100vh;
  background: #fffbf5;
  border-left: 1px solid #e3d5c3;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}
.ai-toggle-btn {
  background: #8b6b4d;
  color: white;
  padding: 12px 16px;
  border-radius: 30px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: transform 0.2s;
}
.ai-toggle-btn:hover {
  transform: scale(1.05);
}
.ai-icon {
  font-size: 18px;
}
.ai-text {
  font-size: 14px;
  font-weight: 500;
}
.ai-sidebar-expanded {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.ai-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e3d5c3;
  background: #fcf8f2;
}
.back-btn {
  background: none;
  border: 1px solid #c9ad8e;
  border-radius: 20px;
  padding: 6px 12px;
  cursor: pointer;
  color: #5a3e2b;
}
.ai-header h3 {
  margin: 0;
  font-size: 16px;
  color: #5a3e2b;
}
.header-placeholder {
  width: 50px;
}
.ai-toolbar {
  display: flex;
  gap: 8px;
  padding: 10px 16px;
  border-bottom: 1px solid #e3d5c3;
  background: #f9f3e9;
}
.tool-btn {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #c9ad8e;
  border-radius: 20px;
  background: #fffbf5;
  cursor: pointer;
  font-size: 13px;
  color: #5a3e2b;
}
.tool-btn.active {
  background: #8b6b4d;
  color: white;
  border-color: #6f523b;
}
.history-list {
  max-height: 200px;
  overflow-y: auto;
  border-bottom: 1px solid #e3d5c3;
  background: #f9f3e9;
}
.history-item {
  padding: 10px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #3e2e1f;
}
.history-item:hover {
  background: #e9dccc;
}
.messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #fffbf5;
}
.empty-chat {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #a28b72;
  text-align: center;
  gap: 8px;
}
.empty-icon {
  font-size: 2.5rem;
}
.empty-chat p {
  margin: 0;
  font-size: 0.95rem;
}
.history-empty {
  padding: 0.6rem 1rem;
  color: #a28b72;
  text-align: center;
  font-style: italic;
}
.message {
  display: flex;
  gap: 8px;
  max-width: 85%;
}
.message.user {
  flex-direction: row-reverse;
  align-self: flex-end;
}
.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #e9dccc;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.bubble {
  padding: 10px 14px;
  border-radius: 18px;
  background: #f1ebe3;
  color: #2c3e50;
  font-size: 14px;
  line-height: 1.4;
  word-wrap: break-word;
  border: 1px solid #e3d5c3;
}
.message.user .bubble {
  background: #8b6b4d;
  color: #fef7e9;
  border-color: #6f523b;
}
.input-area {
  display: flex;
  gap: 10px;
  padding: 12px 16px;
  border-top: 1px solid #e3d5c3;
  background: #fcf8f2;
}
.input-area textarea {
  flex: 1;
  border: 1px solid #dacbb8;
  border-radius: 20px;
  padding: 10px 14px;
  resize: none;
  outline: none;
  font-size: 14px;
  background: #fffbf5;
}
.send-btn {
  padding: 0 16px;
  background: #8b6b4d;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
}
.thinking-bubble {
  background-color: #f1ebe3;
  display: flex;
  align-items: center;
  gap: 3px;
}

.thinking-text {
  color: #8f7b67;
  font-style: italic;
  font-size: 14px;
}

.thinking-dots span {
  display: inline-block;
  font-size: 1.3rem;
  line-height: 1;
  color: #8b6b4d;
  animation: blink 1.4s infinite both;
}

.thinking-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.thinking-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%, 80%, 100% { opacity: 0; }
  40% { opacity: 1; }
}

.footer-note {
  padding: 8px 16px;
  font-size: 12px;
  color: #8f7b67;
  text-align: center;
  border-top: 1px solid #e3d5c3;
  background: #f9f3e9;
}

@media (max-width: 480px) {
  .ai-sidebar.expanded {
    width: 100vw;
    right: 0;
  }
}
</style>