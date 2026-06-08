<template>
  <div>
    <div class="container mt-4">
      <BreadcrumbNav :items="breadcrumbItems" />
      <h2>浏览历史</h2>
      <div v-if="loading">加载中...</div>
      <div v-else-if="histories.length === 0" class="alert alert-info">暂无浏览记录</div>
      <div v-else class="list-group">
        <div v-for="item in histories" :key="item.id" class="list-group-item">
          <router-link v-if="getChapterId(item)" :to="'/chapters/' + getChapterId(item)">
            {{ getBookTitle(item) }} · {{ getChapterTitle(item) }}
          </router-link>
          <span v-else>
            {{ getBookTitle(item) }} · {{ getChapterTitle(item) }}
          </span>
          <small class="text-muted float-end">{{ formatDate(item.viewedAt) }}</small>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import BreadcrumbNav from '@/components/BreadcrumbNav.vue'
import { ElMessage } from 'element-plus'

export default {
  setup() {
    const breadcrumbItems = [
      { label: '首页', path: '/' },
      { label: '浏览历史' }
    ]

    const histories = ref([])
    const loading = ref(true)

    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN', { hour12: false })
    }

    const getChapterId = (item) => item?.chapter?.id || item?.chapterId || null
    const getBookTitle = (item) => item?.bookTitle || item?.book?.title || '未知书籍'
    const getChapterTitle = (item) => item?.chapterTitle || item?.chapter?.title || '未知章节'

    onMounted(async () => {
      try {
        const res = await request.get('/user/history')
        histories.value = res.data
      } catch (error) {
        console.error('获取浏览历史失败', error)
        ElMessage.error('获取浏览历史失败')
      } finally {
        loading.value = false
      }
    })

    return { histories, loading, formatDate, getChapterId, getBookTitle, getChapterTitle, breadcrumbItems }
  }
}
</script>