<template>
  <div class="community-page">
    <BreadcrumbNav :items="breadcrumbItems" />
    <!-- 发布讨论卡片（内联） -->
    <div class="post-card">
      <h3>发起新讨论</h3>
      <form @submit.prevent="handleSubmit">
        <input
            v-model="form.title"
            type="text"
            placeholder="标题"
            required
        />
        <div class="textarea-wrapper">
          <textarea
              v-model="form.content"
              placeholder="内容"
              rows="4"
              maxlength="2000"
              required
          ></textarea>
          <span class="word-count">{{ contentLength }}/2000</span>
        </div>
        <select v-model="form.category">
          <option value="提问">提问</option>
          <option value="心得">心得</option>
          <option value="打卡">打卡</option>
        </select>
        <button type="submit" :disabled="loading">
          {{ loading ? '发布中...' : '发布' }}
        </button>
      </form>
    </div>

    <!-- 选项卡 -->
    <div class="tabs">
      <span :class="{ active: sortType === 'latest' }" @click="switchTab('latest')">最新</span>
      <span :class="{ active: sortType === 'hottest' }" @click="switchTab('hottest')">最热</span>
    </div>

    <!-- 讨论列表 -->
    <DiscussionList :conversations="conversations" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'
import DiscussionList from '@/components/DiscussionList.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'

const breadcrumbItems = [
  { label: '首页', path: '/' },
  { label: '社区' }
]

const userStore = useUserStore()
const conversations = ref([])
const sortType = ref('latest')
const loading = ref(false)
const form = ref({
  title: '',
  content: '',
  category: '提问'
})

const contentLength = computed(() => form.value.content.length)

// 获取讨论列表（适配后端分页返回）
const fetchConversations = async () => {
  try {
    // 根据排序类型决定参数（最新按创建时间降序，最热按点赞数降序）
    const params = {
      page: 0,
      size: 20,
      sort: sortType.value === 'latest' ? 'createdAt,desc' : 'likeCount,desc'
    }
    const response = await request.get('/community/conversations', { params })
    // 后端返回 Page 对象，数据在 content 中
    conversations.value = response.data.content || []
  } catch (error) {
    console.error('获取讨论失败', error)
    ElMessage.error('获取讨论列表失败')
  }
}

// 切换选项卡
const switchTab = (type) => {
  sortType.value = type
  fetchConversations()
}

// 提交新讨论
const handleSubmit = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    // 可跳转至登录页
    return
  }
  try {
    await ElMessageBox.confirm('确认发布该讨论？', '发布确认', {
      confirmButtonText: '发布',
      cancelButtonText: '取消',
      type: 'info'
    })
  } catch {
    return
  }
  loading.value = true
  try {
    await request.post('/community/conversations', form.value)
    ElMessage.success('发布成功')
    // 重置表单
    form.value = { title: '', content: '', category: '提问' }
    // 刷新列表
    await fetchConversations()
  } catch (error) {
    console.error('发布失败', error)
    ElMessage.error('发布失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchConversations()
})
</script>

<style scoped>
.community-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
}

/* 发布卡片样式 */
.post-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 24px;
  margin-bottom: 24px;
}
.post-card h3 {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 1.4rem;
  color: #2c3e50;
}
.textarea-wrapper {
  position: relative;
  margin-bottom: 16px;
}

.textarea-wrapper textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
  box-sizing: border-box;
  resize: vertical;
}

.textarea-wrapper textarea:focus {
  outline: none;
  border-color: #409eff;
}

.word-count {
  position: absolute;
  bottom: 8px;
  right: 12px;
  font-size: 0.8rem;
  color: #999;
  pointer-events: none;
}

.post-card input,
.post-card textarea,
.post-card select {
  width: 100%;
  padding: 12px;
  margin-bottom: 16px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
  box-sizing: border-box;
}
.post-card input:focus,
.post-card textarea:focus,
.post-card select:focus {
  outline: none;
  border-color: #409eff;
}
.post-card button {
  background-color: #409eff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
}
.post-card button:hover {
  background-color: #66b1ff;
}
.post-card button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

/* 选项卡样式 */
.tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  border-bottom: 1px solid #ddd;
  padding-bottom: 0.5rem;
}
.tabs span {
  cursor: pointer;
  padding: 0.5rem 1rem;
}
.tabs span.active {
  color: #007bff;
  border-bottom: 2px solid #007bff;
}
</style>