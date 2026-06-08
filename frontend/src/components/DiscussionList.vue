<template>
  <div class="discussion-list">
    <div
        v-for="item in conversations"
        :key="item.id"
        class="discussion-card"
        @click="goToDetail(item.id)"
    >
      <div class="card-header">
        <span class="category-tag" :class="item.category">{{ item.category }}</span>
        <span class="date">{{ formatDate(item.createdAt) }}</span>
      </div>
      <h4 class="title">{{ item.title }}</h4>
      <p class="excerpt">{{ item.content?.substring(0, 80) }}...</p>
      <div class="card-footer">
        <span class="author">by {{ item.creatorName || '匿名' }}</span>
        <span class="stats">💬 {{ item.messageCount || 0 }} 回复</span>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name: 'DiscussionList',
  props: {
    conversations: {
      type: Array,
      required: true
    }
  },
  setup() {
    const router = useRouter()
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\//g, '-')
    }
    const goToDetail = (id) => {
      router.push(`/community/conversation/${id}`)
    }
    return {
      formatDate,
      goToDetail
    }
  }
}
</script>

<style scoped>
.discussion-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.discussion-card {
  background: var(--card-bg, #ffffff);
  border-radius: var(--border-radius, 12px);
  box-shadow: var(--box-shadow, 0 4px 12px rgba(0, 0, 0, 0.05));
  padding: 20px;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.discussion-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 0.9rem;
}

.category-tag {
  background-color: #ecf5ff;
  color: var(--primary-color, #409eff);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* 分类标签颜色（保留原有分类风格） */
.category-tag.提问 {
  background-color: #e3f2fd;
  color: #1976d2;
}
.category-tag.心得 {
  background-color: #e8f5e8;
  color: #2e7d32;
}
.category-tag.打卡 {
  background-color: #fff3e0;
  color: #f57c00;
}
/* 可扩展其他分类 */
.category-tag.分享 {
  background-color: #fdf6ec;
  color: #e6a23c;
}
.category-tag.讨论 {
  background-color: #f4f4f5;
  color: #909399;
}

.date {
  color: #909399;
}

.title {
  margin: 8px 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
}

.excerpt {
  color: #5a5e66;
  margin-bottom: 12px;
  line-height: 1.5;
  font-size: 0.95rem;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  color: #909399;
  font-size: 0.9rem;
  border-top: 1px solid #ebeef5;
  padding-top: 12px;
}

.author {
  font-weight: 500;
}

.stats {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>