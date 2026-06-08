<template>
  <div class="conversation-detail" v-if="topic">
    <BreadcrumbNav :items="breadcrumbItems" />
    <h2>{{ topic.title }}</h2>
    <div class="topic-content">
      <p>{{ topic.content }}</p>
      <div class="meta">
        <span>{{ topic.user?.username }}</span> · <span>{{ formatDate(topic.createdAt) }}</span>
      </div>
    </div>

    <h3>回复</h3>
    <div class="messages">
      <div v-for="msg in topic.messages" :key="msg.id" class="message">
        <div class="message-header">
          <span class="username">{{ msg.user?.username || '匿名' }}</span>
          <span class="date">{{ formatDate(msg.createdAt) }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
      <div v-if="!topic.messages || topic.messages.length === 0" class="no-reply">
        暂无回复，快来抢沙发～
      </div>
    </div>

    <div class="reply-box">
      <textarea v-model="replyContent" placeholder="写下你的回复..." rows="3"></textarea>
      <button @click="submitReply" :disabled="submitting">
        {{ submitting ? '发表中...' : '发表回复' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const topic = ref(null)
const breadcrumbItems = ref([
  { label: '首页', path: '/' },
  { label: '社区', path: '/community' },
  { label: '加载中...' }
])
const replyContent = ref('')
const submitting = ref(false)

const fetchDetail = async () => {
  const id = route.params.id
  try {
    const res = await request.get(`/community/conversations/${id}`)
    topic.value = res.data
    breadcrumbItems.value = [
      { label: '首页', path: '/' },
      { label: '社区', path: '/community' },
      { label: topic.value.title }
    ]
  } catch (error) {
    console.error('获取主题详情失败', error)
    ElMessage.error('加载失败，请稍后重试')
  }
}

const submitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('回复内容不能为空')
    return
  }
  submitting.value = true
  try {
    const id = route.params.id
    // 发送格式必须为 { content: "xxx" }
    await request.post(`/community/conversations/${id}/messages`, { content: replyContent.value })
    replyContent.value = ''
    // 重新加载主题详情，刷新回复列表
    await fetchDetail()
  } catch (error) {
    console.error('发表回复失败', error)
    ElMessage.error('发表失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const now = new Date()
  const diff = (now - date) / 1000
  if (diff < 60) return '刚刚'
  if (diff < 3600) return `${Math.floor(diff / 60)}分钟前`
  if (diff < 86400) return `${Math.floor(diff / 3600)}小时前`
  if (diff < 2592000) return `${Math.floor(diff / 86400)}天前`
  return `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`
}

onMounted(fetchDetail)
</script>

<style scoped>
.conversation-detail {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
.topic-content {
  border-bottom: 1px solid #ddd;
  padding-bottom: 20px;
  margin-bottom: 20px;
}
.meta {
  color: #666;
  font-size: 0.9em;
  margin-top: 10px;
}
.messages {
  margin-bottom: 30px;
}
.message {
  border: 1px solid #eee;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  background: #fafafa;
}
.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 0.9em;
}
.username {
  font-weight: bold;
  color: #2c3e50;
}
.date {
  color: #999;
}
.message-content {
  line-height: 1.5;
}
.no-reply {
  text-align: center;
  color: #999;
  padding: 20px;
}
.reply-box {
  margin-top: 20px;
}
.reply-box textarea {
  width: 100%;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 5px;
  resize: vertical;
}
.reply-box button {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 5px;
  cursor: pointer;
}
.reply-box button:hover {
  background-color: #66b1ff;
}
.reply-box button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}
</style>