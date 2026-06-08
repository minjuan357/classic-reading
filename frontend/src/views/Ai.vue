<template>
  <div class="ai-fullscreen">
    <div class="ai-container">
      <div class="ai-header">
        <h3>AI伴学助手</h3>
        <button class="close-btn" @click="goBack">← 返回</button>
      </div>

      <!-- 工具栏：新对话、历史记录、深度思考 -->
      <div class="ai-toolbar">
        <button class="tool-btn" @click="newChat">新对话</button>
        <button class="tool-btn" @click="toggleHistory">历史记录</button>
        <button class="tool-btn" :class="{ active: deepThinking }" @click="toggleDeepThinking">深度思考</button>
      </div>

      <!-- 历史记录列表（可折叠） -->
      <div v-if="showHistory" class="history-list">
        <div v-if="conversations.length === 0" class="history-empty">暂无历史记录</div>
        <div v-for="conv in conversations" :key="conv.id" class="history-item" @click="loadConversation(conv.id)">
          {{ conv.title }}
        </div>
      </div>

      <!-- 消息列表 -->
      <div class="messages" ref="messagesContainer">
        <div v-if="messages.length === 0 && !isThinking" class="empty-chat">
          <div class="empty-icon">📖</div>
          <p>你好！我是AI伴学助手</p>
          <p>请告诉我你想学习哪部典籍，或有什么疑问？</p>
        </div>
        <div v-for="(msg, index) in messages" :key="index" class="message" :class="msg.role">
          <div class="avatar">{{ msg.role === 'user' ? '👤' : '📖' }}</div>
          <div class="bubble">{{ msg.content }}</div>
        </div>
        <div v-if="isThinking" class="message assistant">
          <div class="avatar">📖</div>
          <div class="bubble thinking-bubble">
            <span class="thinking-text">正在思考</span>
            <span class="thinking-dots"><span>.</span><span>.</span><span>.</span></span>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <textarea
            v-model="inputText"
            placeholder="请输入您的内容~（Shift+Enter换行，Enter发送）"
            @keydown.enter.prevent="handleEnter"
            rows="1"
        ></textarea>
        <button class="send-btn" @click="sendMessage">发送</button>
      </div>
      <div class="footer-note">注：内容由AI生成，可能有错误，使用前注意查证</div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const showHistory = ref(false)
const inputText = ref('')
const messages = ref([
  { role: 'assistant', content: '你好，我是古籍智能助手，有什么可以帮您？' }
])
const conversations = ref([])
const currentConversationId = ref(null)
const deepThinking = ref(false)
const isThinking = ref(false)

const messagesContainer = ref(null)

const goBack = () => {
  router.back()
}

// 获取历史对话列表
const fetchConversations = async () => {
  if (!userStore.token) return
  try {
    const res = await request.get('/ai/conversations')
    conversations.value = res.data
  } catch (error) {
    console.error('获取历史记录失败', error)
  }
}

// 切换历史记录（登录检查）
const toggleHistory = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再查看历史记录');
    router.push('/login');
    return;
  }
  showHistory.value = !showHistory.value
  if (showHistory.value) {
    fetchConversations()
  }
}

// 创建新对话（登录检查）
const newChat = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再使用AI助手');
    router.push('/login');
    return;
  }
  try {
    const res = await request.post('/ai/conversations', { title: '新对话' })
    currentConversationId.value = res.data.id
    messages.value = [{ role: 'assistant', content: '你好，我是古籍智能助手，有什么可以帮您？' }]
    showHistory.value = false
  } catch (error) {
    console.error('创建新对话失败', error)
  }
}

// 加载对话（登录检查）
const loadConversation = async (convId) => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再查看对话');
    router.push('/login');
    return;
  }
  try {
    const res = await request.get(`/ai/conversations/${convId}/messages`)
    messages.value = res.data.map(msg => ({ role: msg.role, content: msg.content }))
    currentConversationId.value = convId
    showHistory.value = false
    scrollToBottom()
  } catch (error) {
    console.error('加载对话失败', error)
  }
}

// 切换深度思考状态
const toggleDeepThinking = () => {
  deepThinking.value = !deepThinking.value
}

// 发送消息（登录检查 + 增强错误日志）
const sendMessage = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录后再使用AI助手');
    router.push('/login');
    return;
  }
  if (!inputText.value.trim()) return
  const userMsg = { role: 'user', content: inputText.value }
  messages.value.push(userMsg)
  const question = inputText.value
  inputText.value = ''
  scrollToBottom()

  isThinking.value = true
  scrollToBottom()
  try {
    const res = await request.post('/ai/chat', {
      message: question,
      conversationId: currentConversationId.value,
      deepThinking: deepThinking.value
    })
    const aiMsg = { role: 'assistant', content: res.data.reply }
    messages.value.push(aiMsg)
    if (!currentConversationId.value && res.data.conversationId) {
      currentConversationId.value = res.data.conversationId
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
    const errorMsg = { role: 'assistant', content: '网络错误，请稍后重试。' }
    messages.value.push(errorMsg)
  } finally {
    isThinking.value = false
    scrollToBottom()
  }
}

const handleEnter = (e) => {
  if (!e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

onMounted(() => {
  if (userStore.token) {
    fetchConversations()
  }
})
</script>

<style scoped>
.tool-btn.active {
  background-color: #8b6b4d;
  color: white;
  border-color: #6f523b;
}

.ai-fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: #f5efe7;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
  font-family: 'Noto Serif SC', serif;
}

.ai-container {
  width: 800px;
  max-width: 90%;
  height: 90vh;
  display: flex;
  flex-direction: column;
  background: #fffbf5;
  border-radius: 24px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  border: 1px solid #e3d5c3;
}

.ai-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.2rem 2rem;
  border-bottom: 1px solid #e3d5c3;
  background-color: #fcf8f2;
}

.ai-header h3 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 600;
  color: #5a3e2b;
}

.close-btn {
  background: none;
  border: 1px solid #c9ad8e;
  border-radius: 30px;
  padding: 0.4rem 1.2rem;
  font-size: 1rem;
  color: #5a3e2b;
  cursor: pointer;
  transition: background 0.2s;
}

.close-btn:hover {
  background-color: #e9dccc;
}

.ai-toolbar {
  padding: 1rem 2rem;
  border-bottom: 1px solid #e3d5c3;
  display: flex;
  gap: 1rem;
  background-color: #fcf8f2;
}

.tool-btn {
  background: none;
  border: 1px solid #c9ad8e;
  border-radius: 30px;
  padding: 0.4rem 1.2rem;
  font-size: 0.95rem;
  color: #5a3e2b;
  cursor: pointer;
  transition: all 0.2s;
}

.tool-btn:hover {
  background-color: #e9dccc;
  border-color: #8b6b4d;
}

.history-list {
  max-height: 180px;
  overflow-y: auto;
  padding: 0.8rem 2rem;
  border-bottom: 1px solid #e3d5c3;
  background-color: #f9f3e9;
}

.history-item {
  padding: 0.6rem 1rem;
  border-radius: 8px;
  font-size: 0.95rem;
  color: #3e2e1f;
  cursor: pointer;
  transition: background 0.2s;
}

.history-item:hover {
  background-color: #e9dccc;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  background-color: #fffbf5;
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
  font-size: 3rem;
  margin-bottom: 8px;
}

.empty-chat p {
  margin: 0;
  font-size: 1.1rem;
  line-height: 1.6;
}

.history-empty {
  padding: 0.6rem 1rem;
  color: #a28b72;
  text-align: center;
  font-style: italic;
}

.message {
  display: flex;
  gap: 1rem;
}

.message.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #e9dccc;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  flex-shrink: 0;
}

.bubble {
  max-width: 70%;
  padding: 0.8rem 1.4rem;
  border-radius: 24px;
  background-color: #f1ebe3;
  color: #2c3e50;
  word-wrap: break-word;
  font-size: 1rem;
  line-height: 1.5;
  border: 1px solid #e3d5c3;
}

.message.user .bubble {
  background-color: #8b6b4d;
  color: #fef7e9;
  border-color: #6f523b;
}

.input-area {
  display: flex;
  padding: 1.2rem 2rem;
  border-top: 1px solid #e3d5c3;
  gap: 1rem;
  background-color: #fcf8f2;
}

.input-area textarea {
  flex: 1;
  border: 1px solid #dacbb8;
  border-radius: 30px;
  padding: 0.8rem 1.4rem;
  resize: none;
  font-family: 'Noto Serif SC', serif;
  font-size: 1rem;
  outline: none;
  transition: border 0.2s;
  max-height: 100px;
  background-color: #fffbf5;
}

.input-area textarea:focus {
  border-color: #8b6b4d;
}

.send-btn {
  background-color: #8b6b4d;
  color: #fef7e9;
  border: none;
  border-radius: 30px;
  padding: 0.8rem 2rem;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
  font-family: 'Noto Serif SC', serif;
}

.send-btn:hover {
  background-color: #6f523b;
}

.thinking-bubble {
  background-color: #f1ebe3;
  display: flex;
  align-items: center;
  gap: 4px;
}

.thinking-text {
  color: #8f7b67;
  font-style: italic;
}

.thinking-dots span {
  display: inline-block;
  font-size: 1.5rem;
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
  padding: 0.8rem 2rem;
  font-size: 0.85rem;
  color: #8f7b67;
  text-align: center;
  border-top: 1px solid #e3d5c3;
  background-color: #f9f3e9;
}
</style>